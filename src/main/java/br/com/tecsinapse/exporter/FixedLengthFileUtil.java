package br.com.tecsinapse.exporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FixedLengthFileUtil {

    public static List<String> getLines(InputStream inputStream, boolean ignoreFirstLine, Charset charset)
            throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line = null;
            if (ignoreFirstLine) {
                line = br.readLine();
            }

            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return lines;
    }

}