/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.importer;

import static br.com.tecsinapse.dataio.util.Constants.DECIMAL_PRECISION;
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

import lombok.extern.slf4j.Slf4j;

import br.com.tecsinapse.dataio.ExporterFormatter;
import br.com.tecsinapse.dataio.annotation.TableCellMapping;
import br.com.tecsinapse.dataio.annotation.TableCellMappings;
import br.com.tecsinapse.dataio.converter.Converter;
import br.com.tecsinapse.dataio.util.ExporterDateUtils;

@Slf4j
public class ImporterUtils {

    private ImporterUtils() {
    }

    public static final String EMPTY_STRING = "";

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
                    if (annotation instanceof TableCellMapping || annotation instanceof TableCellMappings) {
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
                    public Optional<TableCellMapping> apply(Method method) {
                        checkNotNull(method);
                        return Optional.fromNullable(method.getAnnotation(TableCellMapping.class))
                                .or(getFirstTableCellMapping(method.getAnnotation(TableCellMappings.class), group));
                    }
                })
                .inverse();

        tableCellMappingByMethod = filterEntries(tableCellMappingByMethod, new Predicate<Entry<Method, Optional<TableCellMapping>>>() {
            @Override
            public boolean apply(Entry<Method, Optional<TableCellMapping>> entry) {
                checkNotNull(entry);
                return entry.getValue().isPresent()
                        && any(Lists.newArrayList(entry.getValue().get().groups()), assignableTo(group));
            }
        });

        Multimap<Method, TableCellMapping> methodByTableCellMapping = transformValues(tableCellMappingByMethod, new Function<Optional<TableCellMapping>, TableCellMapping>() {
            @Override
            public TableCellMapping apply(Optional<TableCellMapping> tcmOptional) {
                checkNotNull(tcmOptional);
                return tcmOptional.get();
            }
        });

        return Maps.transformValues(methodByTableCellMapping.asMap(), new Function<Collection<TableCellMapping>, TableCellMapping>() {
            @Override
            public TableCellMapping apply(Collection<TableCellMapping> tcms) {
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
                    public boolean apply(TableCellMapping tcm) {
                        checkNotNull(tcm);
                        return any(Lists.newArrayList(tcm.groups()), assignableTo(group));
                    }
                })
                .first();
    }

    private static Predicate<? super Class<?>> assignableTo(final Class<?> group) {
        return new Predicate<Class<?>>() {
            @Override
            public boolean apply(Class<?> g) {
                checkNotNull(g);
                return g.isAssignableFrom(group);
            }
        };
    }

    public static <T> void parseSpreadsheetCell(Class<? extends Converter> tcc, FormulaEvaluator evaluator, Cell cell, Method method, T instance, ExporterFormatter exporterFormatter, boolean useFormatterToParseValueAsString) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        checkNotNull(method);

        Object value = null;

        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            Class<?> converterInputType = getInputTypeApply(tcc);

            value = getValueOrEmptyAsObject(evaluator, cell, Date.class.equals(converterInputType));

            if (value == null) {
                return;
            }

            Class<?> converterReturnType = getReturnTypeApply(tcc);

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
            method.invoke(instance, converter.apply(toStringNullSafe(value)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Converter<?, ?> converter = tcc.newInstance();
            method.invoke(instance, converter.apply(toStringNullSafe(value)));
        }
    }

    public static Object getValueOrEmptyAsObject(FormulaEvaluator evaluator, Cell cell) {
        return getValueOrEmptyAsObject(evaluator, cell, false);
    }
    public static Object getValueOrEmptyAsObject(FormulaEvaluator evaluator, Cell cell, boolean expectedDate) {
        final CellValue cellValue = safeEvaluteFormula(evaluator, cell);
        if (cellValue == null) {
            return "";
        }
        switch (cellValue.getCellType()) {
            case BOOLEAN:
                return cellValue.getBooleanValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)
                        || (expectedDate && DateUtil.isValidExcelDate(cellValue.getNumberValue()))) {
                    return cell.getDateCellValue();
                }
                BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
                return bd.stripTrailingZeros();
            case STRING:
                return cellValue.getStringValue();
            case ERROR:
                return "ERRO";
            default:
                return "";
        }
    }

    private static CellValue safeEvaluteFormula(FormulaEvaluator evaluator, Cell cell) {
        try {
            return evaluator.evaluate(cell);
        } catch (Exception e) {
            log.warn("Formula evalute error (ignored)", e);
        }
        return null;
    }

    public static String getValueOrEmpty(FormulaEvaluator evaluator, Cell cell, ExporterFormatter exporterFormatter) {
        Object value = getValueOrEmptyAsObject(evaluator, cell);
        if (value instanceof Date) {
            return exporterFormatter.formatByDateType((Date) value);
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

    private static boolean isInstanceOf(Object value, Class<?> targetType) {
        return targetType != null && targetType.isInstance(value);
    }

    private static Method getTypedMethodConverter(Class<?> converter) {
        Method[] converterMethod = converter.getMethods();
        for (Method method : converterMethod) {
            Class<?>[] paramTypes = method.getParameterTypes();
            if (method.getName().equals("apply") && paramTypes.length >0 && !isStringOrObject(paramTypes[0])) {
                return method;
            }
        }
        return null;
    }

    private static Class<?> getTypedToComparePrimitive(Class<?> c) throws NoSuchFieldException, IllegalAccessException {
        return  (Class<?>) c.getField("TYPE").get(null);
    }

    private static boolean isStringOrObject(Class<?> type) {
        return String.class.equals(type) || Object.class.equals(type);
    }

    private static Class<?> getReturnTypeApply(Class<?> converter) {
        Method converterMethod = getTypedMethodConverter(converter);
        return converterMethod == null ? null : converterMethod.getReturnType();
    }

    private static Class<?> getInputTypeApply(Class<?> converter) {
        Method converterMethod = getTypedMethodConverter(converter);
        return converterMethod == null ? null : converterMethod.getParameterTypes()[0];
    }

    private static Class<?> getMethodParamType(Method method) {
        Class<?>[] inputParamsType = method.getParameterTypes();
        if (inputParamsType.length > 0) {
            return inputParamsType[0];
        }
        return null;
    }

    private static boolean isSameClassOrExtendedNullSafe(Class<?> c1, Class<?> c2) throws NoSuchFieldException, IllegalAccessException {
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

    private static String toStringNullSafe(Object o) {
        return o == null ? EMPTY_STRING : o.toString();
    }

}
