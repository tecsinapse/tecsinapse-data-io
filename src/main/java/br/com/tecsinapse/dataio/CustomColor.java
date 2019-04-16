package br.com.tecsinapse.dataio;

import java.awt.Color;

import org.apache.poi.hssf.util.HSSFColor;

import lombok.Getter;

@Getter
class CustomColor extends HSSFColor {

    private short index;
    private final short[] triplet;
    private final String hexString;

    CustomColor(short index, Color color) {
        this.index = index;
        triplet = new short[] { (short)color.getRed(),
                (short)color.getGreen(),
                (short)color.getBlue()
        };
        hexString = String.format("%s:%s:%s", intToHexStr(color.getRed()),
                intToHexStr(color.getGreen()),
                intToHexStr(color.getBlue())) ;
    }

    private static String intToHexStr(int i) {
        String hex = Integer.toHexString(i);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex + hex;
    }

}
