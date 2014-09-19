package br.com.tecsinapse.exporter.test;

import java.util.Objects;

import org.joda.time.LocalDate;

import br.com.tecsinapse.exporter.annotation.FixedLengthColumn;
import br.com.tecsinapse.exporter.converter.IntegerTableCellConverter;
import br.com.tecsinapse.exporter.converter.LocalDateTableCellConverter;

public class FakeFixedLengthFilePojo {

    private int one;
    private String two;
    private LocalDate three;

    public FakeFixedLengthFilePojo() {
    }

    public FakeFixedLengthFilePojo(int one, String two, LocalDate three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    @FixedLengthColumn(columnIndex = 0, columnSize = 2, converter = IntegerTableCellConverter.class)
    public void setOne(int one) {
        this.one = one;
    }

    @FixedLengthColumn(columnIndex = 1, columnSize = 10)
    public void setTwo(String two) {
        this.two = two;
    }

    @FixedLengthColumn(columnIndex = 2, columnSize = 10, converter = LocalDateTableCellConverter.class)
    public void setThree(LocalDate three) {
        this.three = three;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FakeFixedLengthFilePojo)) {
            return false;
        }
        FakeFixedLengthFilePojo other = (FakeFixedLengthFilePojo) obj;
        return Objects.equals(one, other.one) && Objects.equals(two, other.two) && Objects.equals(three, other.three);
    }

    @Override
    public String toString() {
        return "FakeFixedLengthFilePojo{" + "one='" + one + '\'' + ", two='" + two + '\'' + ", three='" + three + '\''
                + '}';
    }
}