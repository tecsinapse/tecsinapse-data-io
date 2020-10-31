package br.com.tecsinapse.dataio.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CommonUtils {

    public static final String EMPTY_STRING = "";

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String nullToEmpty(String str) {
        return str == null ? EMPTY_STRING : str;
    }

    public static String repeat(String str, int size) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        StringBuilder strOut = new StringBuilder();
        for (int i = 0; i < size; i += str.length()) {
            strOut.append(str);
        }
        return strOut.substring(0, size);
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

}
