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
    
    public static List<String> getLines(InputStream inputStream, boolean ignoreFirstLine, Charset charset)
            throws IOException {
    	return getLines(inputStream, ignoreFirstLine, 0, null, charset);
    }
        
    public static List<String> getLines(InputStream inputStream, boolean ignoreFirstLine, int afterLine, String eofCharacter, Charset charset) throws IOException{
    	
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line = null;
            
            afterLine += (ignoreFirstLine ? 1 : 0);
            
            for(int i = 0; i < afterLine; i++){
            	line = br.readLine();
            }

            line = br.readLine();
            while (!isEof(line, eofCharacter)) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw e;
        }
        return lines;
    }
    
    private static boolean isEof(String line, String eofCharacter){
    	return line == null || (eofCharacter != null && line.contains(eofCharacter));
    }
}