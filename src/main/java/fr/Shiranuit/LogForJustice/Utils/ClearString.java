package fr.Shiranuit.LogForJustice.Utils;

import java.util.ArrayList;

public class ClearString {
    public static String format(String text) {
        ArrayList<String> bytes = new ArrayList<String>();
        for (int i=0; i<text.length(); i++) {
           char c = text.charAt(i);
           int ascii = (int)c;
           bytes.add(ascii+"");
        }
        return String.join("|",bytes);
    }

    public static String parse(String text) {
        String[] data = text.split("[|]");
        String result = "";
        for (int i=0; i<data.length; i++) {
            result += (char)Integer.valueOf(data[i]).intValue();
        }
        return result;
    }
}