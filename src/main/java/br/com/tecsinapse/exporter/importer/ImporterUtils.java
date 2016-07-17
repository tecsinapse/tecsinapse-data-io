/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

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
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
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
import br.com.tecsinapse.exporter.converter.TableCellConverter;

public class ImporterUtils {

    private static short DECIMAL_PRECISION = 10;

    private static final LocalDate LOCAL_DATE_BIGBANG = LocalDate.fromDateFields(DateUtil.getJavaDate(0.0));


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

    public static <T> void parseSpreadsheetCell(Class<? extends TableCellConverter<?>> tcc, FormulaEvaluator evaluator, Cell cell, Method method, T instance, ExporterFormatter exporterFormatter) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        checkNotNull(method);
        Object value = getValueOrEmptyAsObject(evaluator, cell);
        try {
            if (value == null) {
                return;
            }
            Class<?> targetType = getReturnType(tcc);
            if (isInstanceOf(value, targetType)) {
                method.invoke(instance, value);
                return;
            }
            if (isInstanceOf(value, BigDecimal.class)) {
                Object numericValue = toNumericValue((BigDecimal) value, targetType);
                if (numericValue != null) {
                    method.invoke(instance, numericValue);
                    return;
                }
            }
            if (isInstanceOf(value, LocalDateTime.class) || isInstanceOf(targetType, LocalDateTime.class)) {
                Object dateTimeValue = toDateTimeValue((LocalDateTime) value, targetType, exporterFormatter);
                if (dateTimeValue != null) {
                    method.invoke(instance, dateTimeValue);
                    return;
                }
            }
            TableCellConverter<?> converter = tcc.newInstance();
            method.invoke(instance, converter.apply(value.toString()));
        } catch (NoSuchMethodException e) {
            TableCellConverter<?> converter = tcc.newInstance();
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
                    LocalDateTime localDateTime = LocalDateTime.fromDateFields(cell.getDateCellValue());
                    return localDateTime;
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
        if (value instanceof LocalDateTime) {
            return formatLocalDateTimeAsString((LocalDateTime) value, exporterFormatter);
        }
        if (value instanceof BigDecimal) {
            return formatNumericAsString((BigDecimal) value, exporterFormatter);
        }
        return value.toString();
    }

    private static Object toNumericValue(BigDecimal bigDecimal, Class<?> targetType) {
        if (Integer.class.equals(targetType)) {
            return bigDecimal.intValue();
        }
        if (Long.class.equals(targetType)) {
            return bigDecimal.longValue();
        }
        return null;
    }

    private static Object toDateTimeValue(LocalDateTime localDateTime, Class<?> targetType, ExporterFormatter exporterFormatter) {
        if (LocalDateTime.class.equals(targetType)) {
            return localDateTime;
        }
        if (LocalDate.class.equals(targetType)) {
            return localDateTime.toLocalDate();
        }
        if (LocalTime.class.equals(targetType)) {
            return localDateTime.toLocalTime();
        }
        if (String.class.equals(targetType)) {
            return formatLocalDateTimeAsString(localDateTime, exporterFormatter);
        }
        return null;
    }

    private static String formatLocalDateTimeAsString(LocalDateTime localDateTime, ExporterFormatter exporterFormatter) {
        LocalTime localTime = localDateTime.toLocalTime();
        LocalDate localDate = localDateTime.toLocalDate();
        if (LocalTime.MIDNIGHT.equals(localTime)) {
            return exporterFormatter.formatLocalDate(localDate);
        }

        if (LOCAL_DATE_BIGBANG.equals(localDate)) {
            return exporterFormatter.formatLocalTime(localTime);
        }
        return exporterFormatter.formatLocalDateTime(localDateTime);
    }

    private static String formatNumericAsString(Number number, ExporterFormatter exporterFormatter) {
        if (number == null) {
            return null;
        }
        return exporterFormatter.formatNumber(number);
    }

    private static boolean isInstanceOf(Object value, Class<?> targetType) throws NoSuchMethodException {
        return value != null && targetType.isInstance(value);
    }

    private static Class<?> getReturnType(Class<?> converter) throws NoSuchMethodException {
        Method converterMethod = converter.getMethod("apply", String.class);
        return converterMethod.getReturnType();
    }
}
