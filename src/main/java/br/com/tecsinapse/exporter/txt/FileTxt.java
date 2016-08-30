/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.txt;

import java.util.ArrayList;
import java.util.List;

import br.com.tecsinapse.exporter.type.SeparatorType;

public class FileTxt {

    private List<List<FieldTxt>> fields = new ArrayList<List<FieldTxt>>();
    private boolean endsWithSeparator = false;

    public void addNewLine() {
        fields.add(new ArrayList<FieldTxt>());
    }

    public void add(String content) {
        add(FieldTxt.newBuilder()
                .withContent(content)
                .build());
    }

    public void addTabbed(String content) {
        add(FieldTxt.newBuilder()
                .withSeparator(SeparatorType.TAB)
                .withContent(content)
                .build());
    }

    public void addTabbedFixedSize(String content, int fixedSize) {
        add(FieldTxt.newBuilder()
                .withSeparator(SeparatorType.TAB)
                .withContent(content)
                .withFixedSize(fixedSize)
                .build());
    }

    public void add(FieldTxt field) {
        getLastLine().add(field);
    }

    public List<FieldTxt> getLastLine() {
        return fields.get(getLastLineIndex());
    }

    public Integer getLastLineIndex() {
        return fields.size() - 1;
    }

    public List<List<FieldTxt>> getFields() {
        return fields;
    }

    public void setFields(List<List<FieldTxt>> fields) {
        this.fields = fields;
    }

    public boolean isEndsWithSeparator() {
        return endsWithSeparator;
    }

    public void setEndsWithSeparator(boolean endsWithSeparator) {
        this.endsWithSeparator = endsWithSeparator;
    }

}
