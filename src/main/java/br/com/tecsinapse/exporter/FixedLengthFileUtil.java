package br.com.tecsinapse.exporter;

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
    
    public static List<String> getLines(InputStream inputStream, boolean ignoreFirstLine, int afterLine, String eofCharacter, Charset charset) throws IOException{
    	
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line = null;
            if (ignoreFirstLine) {
                line = br.readLine();
            }
            
            for(int i = 0; i < afterLine; i++){
            	line = br.readLine();
            }

            line = br.readLine();
            while (!isEOF(line, eofCharacter)) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw e;
        }
        return lines;
    }
    
    private static boolean isEOF(String line, String eofCharacter){
    	return line == null || (eofCharacter != null && line.contains(eofCharacter));
    }
}