/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.txt;

import com.google.common.base.Strings;

/**
 * Representa o alinhamento que o campo deve utilizar dentro do arquivo
 *
 * <ul>
 * <li>RIGHT: À Direita (Quando utilizar tamanho fixo irá preencher com o caracter especificado no FieldTxt à esquerda até completar o tamanho)</li>
 * <li>LEFT: À Esquerda (Quando utilizar tamanho fixo irá preencher com o caracter especificado no FieldTxt à direita até completar o tamanho)</li>
 * </ul>
 */
public enum FieldTxtAlign {
    RIGHT {
        @Override
        public String getValueAligned(FieldTxt field) {
            String content = getContent(field);
            int size = field.getFixedSize();
            return size == 0 ? content : String.format("%s%s", Strings.repeat(field.getFiller(), size - content.length()), content);
        }
    },
    LEFT {
        @Override
        public String getValueAligned(FieldTxt field) {
            String content = getContent(field);
            int size = field.getFixedSize();
            return size == 0 ? content : String.format("%s%s", content, Strings.repeat(field.getFiller(), size - content.length()));
        }
    };

    private static String getContent(FieldTxt field) {
        String content = field.getContent();
        int size = field.getFixedSize();
        if (size > 0 && content.length() > size) {
            content = content.substring(0, size);
        }
        return content;
    }

    public abstract String getValueAligned(FieldTxt field);

}
