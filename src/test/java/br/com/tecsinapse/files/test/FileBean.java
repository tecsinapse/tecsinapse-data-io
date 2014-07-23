package br.com.tecsinapse.files.test;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.BigDecimalTableCellConverter;
import br.com.tecsinapse.exporter.converter.IntegerFromBigDecimalTableCellConverter;
import br.com.tecsinapse.exporter.converter.IntegerTableCellConverter;
import br.com.tecsinapse.files.test.ImporterFileTest.LocalDateConverter;

/**
* Created by janario on 7/23/14.
*/
public final class FileBean {

    String cidade;
    String estado;
    LocalDate data;
    String vazia;
    Integer inteiro;
    BigDecimal decimal;
    Integer numeroInteger;

    FileBean() {
    }

    public FileBean(String cidade, String estado, LocalDate data, String vazia, Integer inteiro, BigDecimal decimal, Integer numeroInteger) {
        this.cidade = cidade;
        this.estado = estado;
        //TODO :36
//            this.data = data;
        this.vazia = vazia;
        this.inteiro = inteiro;
        this.decimal = decimal;
        this.numeroInteger = numeroInteger;
    }

    @TableCellMapping(columnIndex = 0)
    private void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @TableCellMapping(columnIndex = 1)
    private void setEstado(String estado) {
        this.estado = estado;
    }

    @TableCellMapping(columnIndex = 2, converter = LocalDateConverter.class)
    private void setData(LocalDate data) {
        this.data = data;
    }

    @TableCellMapping(columnIndex = 3)
    private void setVazia(String vazia) {
        this.vazia = vazia;
    }

    @TableCellMapping(columnIndex = 4, converter = IntegerTableCellConverter.class)
    private void setInteiro(Integer inteiro) {
        this.inteiro = inteiro;
    }

    @TableCellMapping(columnIndex = 5, converter = BigDecimalTableCellConverter.class)
    private void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    @TableCellMapping(columnIndex = 4, converter = IntegerFromBigDecimalTableCellConverter.class)
    public void setNumeroInteger(Integer numeroInteger) {
        this.numeroInteger = numeroInteger;
    }
}
