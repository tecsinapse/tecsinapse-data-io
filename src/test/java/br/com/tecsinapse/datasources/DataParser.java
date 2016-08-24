/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.datasources;

import java.math.BigDecimal;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.testng.Assert;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.BigDecimalTableCellConverter;
import br.com.tecsinapse.exporter.converter.IntegerTableCellConverter;
import br.com.tecsinapse.exporter.converter.LocalDateTableCellConverter;
import br.com.tecsinapse.exporter.converter.LocalDateTimeTableCellConverter;
import br.com.tecsinapse.exporter.converter.LocalTimeTableCellConverter;
import br.com.tecsinapse.exporter.converter.StringTableCellConverter;

public class DataParser implements ApplyTest<DataParser> {

    private LocalDate date;
    private LocalDateTime dateTime;
    private BigDecimal decimal;
    private Integer integer;
    private int integerPrimitive;
    private String string;
    private String empty;
    private LocalTime time;

    private DataParser actual;
    private String identifier;

    public DataParser() {
    }

    public DataParser(String date, String dateTime, String decimal, Integer integer, String string, String empty, String time) {
        this.date = LocalDate.parse(date);
        this.dateTime = LocalDateTime.parse(dateTime);
        this.decimal = decimal != null ? new BigDecimal(decimal) : null;
        this.integer = integer;
        this.string = string;
        this.empty = empty;
        this.time = LocalTime.parse(time);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public Integer getInteger() {
        return integer;
    }

    public String getString() {
        return string;
    }

    public String getEmpty() {
        return empty;
    }

    public LocalTime getTime() {
        return time;
    }

    @TableCellMapping(columnIndex = 0, converter = LocalDateTableCellConverter.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @TableCellMapping(columnIndex = 1, converter = LocalDateTimeTableCellConverter.class)
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @TableCellMapping(columnIndex = 2, converter = BigDecimalTableCellConverter.class)
    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    @TableCellMapping(columnIndex = 3, converter = IntegerTableCellConverter.class)
    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    @TableCellMapping(columnIndex = 3, converter = IntegerTableCellConverter.class)
    public void setIntegerPrimitive(int integerPrimitive) {
        this.integerPrimitive = integerPrimitive;
    }


    @TableCellMapping(columnIndex = 4, converter = StringTableCellConverter.class)
    public void setString(String string) {
        this.string = string;
    }

    @TableCellMapping(columnIndex = 5)
    public void setEmpty(String empty) {
        this.empty = empty;
    }

    @TableCellMapping(columnIndex = 6, converter = LocalTimeTableCellConverter.class)
    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public void test() {
        Assert.assertEquals(actual.getDate(), getDate(), identifier);
        Assert.assertEquals(actual.getDateTime(), getDateTime(), identifier);
        Assert.assertEquals(actual.getDecimal(), getDecimal(), identifier);
        Assert.assertEquals(actual.getInteger(), getInteger(), identifier);
        Assert.assertEquals(actual.getString(), getString(), identifier);
        Assert.assertEquals(actual.getEmpty(), getEmpty(), identifier);
        Assert.assertEquals(actual.getTime(), getTime(), identifier);
    }

    @Override
    public void setActual(DataParser actual, String identifier) {
        this.actual = actual;
        this.identifier = identifier;
    }
}
