/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.math.BigDecimal;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.BigDecimalTableCellConverter;
import br.com.tecsinapse.exporter.converter.IntegerTableCellConverter;
import br.com.tecsinapse.exporter.converter.LocalDateTableCellConverter;
import br.com.tecsinapse.exporter.converter.LocalDateTimeTableCellConverter;
import br.com.tecsinapse.exporter.converter.LocalTimeTableCellConverter;
import br.com.tecsinapse.exporter.converter.StringTableCellConverter;

public class DataParser {


    private LocalDate date;
    private LocalDateTime dateTime;
    private BigDecimal decimal;
    private Integer integer;
    private String string;
    private String empty;
    private LocalTime time;

    public DataParser() {
    }

    public DataParser(LocalDate date, LocalDateTime dateTime, String decimal, Integer integer, String string, String empty, LocalTime time) {
        this.date = date;
        this.dateTime = dateTime;
        this.decimal = decimal != null ? new BigDecimal(decimal) : null;
        this.integer = integer;
        this.string = string;
        this.empty = empty;
        this.time = time;
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
}
