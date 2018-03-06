package br.com.tecsinapse.datasources;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.IntegerTableCellConverter;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"col1", "col2", "col3"})
@ToString(of = {"col1", "col2", "col3"})
public class ThreeColumnValue {
    private Integer col1;
    private Integer col2;
    private Integer col3;

    public ThreeColumnValue(Integer col1, Integer col2, Integer col3) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
    }

    @TableCellMapping(columnIndex = 0, converter = IntegerTableCellConverter.class)
    public void setCol1(Integer col1) {
        this.col1 = col1;
    }

    @TableCellMapping(columnIndex = 1, converter = IntegerTableCellConverter.class)
    public void setCol2(Integer col2) {
        this.col2 = col2;
    }

    @TableCellMapping(columnIndex = 2, converter = IntegerTableCellConverter.class)
    public void setCol3(Integer col3) {
        this.col3 = col3;
    }
}
