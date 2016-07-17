/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.files.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public String cidade;
    public String estado;
    public LocalDate data;
    public String vazia;
    public Integer inteiro;
    public BigDecimal decimal;
    public Integer numeroInteger;

    FileBean() {
    }

    public FileBean(String cidade, String estado, LocalDate data, String vazia, Integer inteiro, BigDecimal decimal, Integer numeroInteger) {
        this.cidade = cidade;
        this.estado = estado;
        this.data = data;
        this.vazia = vazia;
        this.inteiro = inteiro;
        this.decimal = decimal;
        this.numeroInteger = numeroInteger;
    }

    static List<FileBean> getBeans() {
        //Cidade;Estado;Data;;Número
        List<FileBean> esperados = new ArrayList<>();
        //Pernambuco;PE;01/01/14;;10
        esperados.add(new FileBean("Pernambuco", "PE", new LocalDate(2014, 1, 1), "", 10, new BigDecimal("10.9"), 10));
        //Campo Grande;MS;02/01/14;;11
        esperados.add(new FileBean("Campo Grande", "MS", new LocalDate(2014, 1, 2), "", 11, new BigDecimal("11.8"), 11));
        //Rio de Janeiro;RJ;03/01/14;;12
        esperados.add(new FileBean("Rio de Janeiro", "RJ", new LocalDate(2014, 1, 3), "", 12, new BigDecimal("12.7"), 12));
        //São Paulo;SP;04/01/14;;13
        esperados.add(new FileBean("São Paulo", "SP", new LocalDate(2014, 1, 4), "", 13, new BigDecimal("13.6"), 13));
        //São Paulo;SP;05/01/14;;14
        esperados.add(new FileBean("São Paulo", "SP", new LocalDate(2014, 1, 5), "", 14, new BigDecimal("14.5"), 14));
        return esperados;
    }

    @TableCellMapping(columnIndex = 0)
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @TableCellMapping(columnIndex = 1)
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @TableCellMapping(columnIndex = 2, converter = LocalDateConverter.class)
    public void setData(LocalDate data) {
        this.data = data;
    }

    @TableCellMapping(columnIndex = 3)
    public void setVazia(String vazia) {
        this.vazia = vazia;
    }

    @TableCellMapping(columnIndex = 4, converter = IntegerTableCellConverter.class)
    public void setInteiro(Integer inteiro) {
        this.inteiro = inteiro;
    }

    @TableCellMapping(columnIndex = 5, converter = BigDecimalTableCellConverter.class)
    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    @TableCellMapping(columnIndex = 4, converter = IntegerFromBigDecimalTableCellConverter.class)
    public void setNumeroInteger(Integer numeroInteger) {
        this.numeroInteger = numeroInteger;
    }
}
