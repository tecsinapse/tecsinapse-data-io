/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class FixedLengthFileUtil {

    private FixedLengthFileUtil() {
    }

    public static List<String> getLines(InputStream inputStream, boolean ignoreFirstLine, Charset charset) throws IOException {
        return getLines(inputStream, ignoreFirstLine, 0, null, charset);
    }

    private static List<String> getLines(InputStream inputStream, boolean ignoreFirstLine, int afterLine, String eofCharacter, Charset charset) throws IOException {
        return getLines(inputStream, ignoreFirstLine, afterLine, 0, eofCharacter, charset);
    }

    public static List<String> getLines(InputStream inputStream, boolean ignoreFirstLine, int afterLine, int ignoreLastLines, String eofCharacter, Charset charset) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;

            afterLine += (ignoreFirstLine ? 1 : 0);

            for (int i = 0; i < afterLine; i++) {
                line = br.readLine();
            }

            line = br.readLine();
            while (!isEof(line, eofCharacter)) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
                line = br.readLine();
            }
        }

        if (ignoreLastLines > 0 && lines.size() >= ignoreLastLines) {
            lines = lines.subList(0, lines.size() - ignoreLastLines);
        }

        return lines;
    }

    private static boolean isEof(String line, String eofCharacter) {
        return line == null || (eofCharacter != null && line.contains(eofCharacter));
    }

}