/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import static br.com.tecsinapse.exporter.util.Constants.DECIMAL_PRECISION;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Multimaps.filterEntries;
import static com.google.common.collect.Multimaps.transformValues;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.reflections.ReflectionUtils;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import br.com.tecsinapse.exporter.ExporterFormatter;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.annotation.TableCellMappings;
import br.com.tecsinapse.exporter.converter.Converter;
import br.com.tecsinapse.exporter.util.ExporterDateUtils;

public class ImporterUtils {

    public static <T> void removeBlankLinesOfEnd(List<T> resultList, Class<T> clazz) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
        Collections.reverse(resultList);
        final PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        final Set<Method> readMethodsOfWriteMethodsWithTableCellMapping = FluentIterable.from(Arrays.asList(propertyDescriptors))
                .filter(hasWriteAndReadMethod())
                .filter(hasAnnotationTableCellMapping())
                .transform(toReadMethod())
                .toSet();

        if (!readMethodsOfWriteMethodsWithTableCellMapping.isEmpty()) {
            final Iterator<T> iterator = resultList.iterator();
            while (iterator.hasNext()) {
                final T instance = iterator.next();
                if (allPropertiesHasNoValue(instance, readMethodsOfWriteMethodsWithTableCellMapping)) {
                    iterator.remove();
                } else {
                    break;
                }
            }
        }
        Collections.reverse(resultList);
    }

    private static Predicate<PropertyDescriptor> hasAnnotationTableCellMapping() {
        return new Predicate<PropertyDescriptor>() {
            @Override
            public boolean apply(PropertyDescriptor propertyDescriptor) {
                if (propertyDescriptor == null) {
                    return false;
                }
                final Method writeMethod = propertyDescriptor.getWriteMethod();
                for (Annotation annotation : writeMethod.getDeclaredAnnotations()) {
                    if (annotation instanceof TableCellMapping) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private static Predicate<PropertyDescriptor> hasWriteAndReadMethod() {
        return new Predicate<PropertyDescriptor>() {
            @Override
            public boolean apply(PropertyDescriptor propertyDescriptor) {
                return propertyDescriptor != null && propertyDescriptor.getWriteMethod() != null &&
                        propertyDescriptor.getReadMethod() != null;
            }
        };
    }

    private static <T> boolean allPropertiesHasNoValue(T instance, Set<Method> readMethodsOfWriteMethodsWithTableCellMapping) throws InvocationTargetException, IllegalAccessException {
        for (Method method : readMethodsOfWriteMethodsWithTableCellMapping) {
            final Object value = method.invoke(instance);
            if (method.getReturnType().equals(String.class)) {
                String valueStr = nullToEmpty((String) value).trim();
                if (!isNullOrEmpty(valueStr)) {
                    return false;
                }
            } else if (value != null) {
                return false;
            }
        }
        return true;
    }

    private static Function<PropertyDescriptor, Method> toReadMethod() {
        return new Function<PropertyDescriptor, Method>() {
            @Override
            public Method apply(PropertyDescriptor propertyDescriptor) {
                return propertyDescriptor != null ? propertyDescriptor.getReadMethod() : null;
            }
        };
    }

    public static final Map<Method, TableCellMapping> getMappedMethods(Class<?> clazz, final Class<?> group) {

        Set<Method> cellMappingMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(TableCellMapping.class));
        cellMappingMethods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(TableCellMappings.class)));


        Multimap<Method, Optional<TableCellMapping>> tableCellMappingByMethod = FluentIterable.from(cellMappingMethods)
                .index(new Function<Method, Optional<TableCellMapping>>() {
                    @Override
                    public Optional<TableCellMapping> apply(@Nonnull Method method) {
                        checkNotNull(method);
                        return Optional.fromNullable(method.getAnnotation(TableCellMapping.class))
                                .or(getFirstTableCellMapping(method.getAnnotation(TableCellMappings.class), group));
                    }
                })
                .inverse();

        tableCellMappingByMethod = filterEntries(tableCellMappingByMethod, new Predicate<Entry<Method, Optional<TableCellMapping>>>() {
            @Override
            public boolean apply(@Nonnull Entry<Method, Optional<TableCellMapping>> entry) {
                checkNotNull(entry);
                return entry.getValue().isPresent()
                        && any(Lists.newArrayList(entry.getValue().get().groups()), assignableTo(group));
            }
        });

        Multimap<Method, TableCellMapping> methodByTableCellMapping = transformValues(tableCellMappingByMethod, new Function<Optional<TableCellMapping>, TableCellMapping>() {
            @Override
            public TableCellMapping apply(@Nonnull Optional<TableCellMapping> tcmOptional) {
                checkNotNull(tcmOptional);
                return tcmOptional.get();
            }
        });

        return Maps.transformValues(methodByTableCellMapping.asMap(), new Function<Collection<TableCellMapping>, TableCellMapping>() {
            @Override
            public TableCellMapping apply(@Nonnull Collection<TableCellMapping> tcms) {
                checkNotNull(tcms);
                return Iterables.getFirst(tcms, null);
            }
        });
    }

    private static Optional<TableCellMapping> getFirstTableCellMapping(TableCellMappings tcms, final Class<?> group) {
        if (tcms == null) {
            return Optional.absent();
        }

        return FluentIterable.from(Lists.newArrayList(tcms.value()))
                .filter(new Predicate<TableCellMapping>() {
                    @Override
                    public boolean apply(@Nonnull TableCellMapping tcm) {
                        checkNotNull(tcm);
                        return any(Lists.newArrayList(tcm.groups()), assignableTo(group));
                    }
                })
                .first();
    }

    private static Predicate<? super Class<?>> assignableTo(final Class<?> group) {
        return new Predicate<Class<?>>() {
            @Override
            public boolean apply(@Nonnull Class<?> g) {
                checkNotNull(g);
                return g.isAssignableFrom(group);
            }
        };
    }

    public static <T> void parseSpreadsheetCell(Class<? extends Converter> tcc, FormulaEvaluator evaluator, Cell cell, Method method, T instance, ExporterFormatter exporterFormatter, boolean useFormatterToParseValueAsString) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        checkNotNull(method);
        Object value = getValueOrEmptyAsObject(evaluator, cell);
        try {
            if (value == null) {
                return;
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Class<?> converterReturnType = getReturnTypeApply(tcc);
            Class<?> converterInputType = getInputTypeApply(tcc);

            Class<?> methodInputType = getMethodParamType(method);

            if (isInstanceOf(value, converterInputType) && isSameClassOrExtendedNullSafe(converterReturnType, methodInputType)) {
                Converter converter = tcc.newInstance();
                method.invoke(instance, converter.apply(value));
                return;
            }
            if (isInstanceOf(value, Date.class)) {
                if (useFormatterToParseValueAsString) {
                    value = exporterFormatter.formatByType(value, false);
                } else {
                    Date date = (Date) value;
                    value = ExporterDateUtils.formatWithIsoByDateType(date);
                }
            }

            if (isInstanceOf(value, BigDecimal.class)) {
                if (useFormatterToParseValueAsString) {
                    value = exporterFormatter.formatByType(value, false);
                } else {
                    BigDecimal bigDecimal = (BigDecimal) value;
                    value = bigDecimal.toPlainString();
                }
            }

            Converter<?, ?> converter = tcc.newInstance();
            method.invoke(instance, converter.apply(value.toString()));
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            Converter<?, ?> converter = tcc.newInstance();
            method.invoke(instance, converter.apply(value.toString()));
        }
    }

    public static Object getValueOrEmptyAsObject(FormulaEvaluator evaluator, Cell cell) {
        final CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null) {
            return "";
        }
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cellValue.getBooleanValue());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return date;
                }
                BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
                return bd.stripTrailingZeros();
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
            case Cell.CELL_TYPE_ERROR:
                return "ERRO";
            default:
                return "";
        }
    }

    public static String getValueOrEmpty(FormulaEvaluator evaluator, Cell cell, ExporterFormatter exporterFormatter) {
        Object value = getValueOrEmptyAsObject(evaluator, cell);
        if (value instanceof Date) {
            exporterFormatter.formatByDateType((Date) value);
        }
        if (value instanceof BigDecimal) {
            return formatNumericAsString((BigDecimal) value, exporterFormatter);
        }
        return value.toString();
    }

    private static String formatNumericAsString(Number number, ExporterFormatter exporterFormatter) {
        if (number == null) {
            return null;
        }
        return exporterFormatter.formatNumber(number);
    }

    private static boolean isInstanceOf(Object value, Class<?> targetType) throws NoSuchMethodException {
        return value != null && targetType != null && targetType.isInstance(value);
    }

    private static Method getTypedMethodConverter(Class<?> converter) throws NoSuchMethodException {
        Method converterMethod[] = converter.getMethods();
        for (Method method : converterMethod) {
            Class<?>[] paramTypes = method.getParameterTypes();
            if (method.getName().equals("apply") && paramTypes.length >0 && !isStringOrObject(paramTypes[0])) {
                return method;
            }
        }
        return null;
    }

    private static Class<?> getTypedToComparePrimitive(Class<?> c) throws NoSuchFieldException, IllegalAccessException {
        Class<?> classz = (Class<?>) c.getField("TYPE").get(null);
        return classz;
    }

    private static boolean isStringOrObject(Class<?> type) {
        return String.class.equals(type) || Object.class.equals(type);
    }

    private static Class<?> getReturnTypeApply(Class<?> converter) throws NoSuchMethodException {
        Method converterMethod = getTypedMethodConverter(converter);
        return converterMethod == null ? null : converterMethod.getReturnType();
    }

    private static Class<?> getInputTypeApply(Class<?> converter) throws NoSuchMethodException {
        Method converterMethod = getTypedMethodConverter(converter);
        return converterMethod == null ? null : converterMethod.getParameterTypes()[0];
    }

    private static Class<?> getMethodParamType(Method method) throws NoSuchMethodException {
        Class<?>[] inputParamsType = method.getParameterTypes();
        if (inputParamsType.length > 0) {
            return inputParamsType[0];
        }
        return null;
    }

    private static boolean isSameClassOrExtendedNullSafe(Class<?> c1, Class<?> c2) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        if (c1 == null || c2 == null) {
            return false;
        }
        if (c1.isPrimitive()) {
            return comparePrimitive(c2, c1);
        }
        if (c2.isPrimitive()) {
            return comparePrimitive(c1, c2);
        }
        return c1.equals(c2) || c2.isAssignableFrom(c1);
    }

    private static boolean comparePrimitive(Class<?> c1, Class<?> primitive) throws NoSuchFieldException, IllegalAccessException {
        Class<?> classz = getTypedToComparePrimitive(c1);
        return classz != null && classz.equals(primitive);
    }

}
