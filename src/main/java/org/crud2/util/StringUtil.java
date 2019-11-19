package org.crud2.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {
    public static String fromInputStream(InputStream inputStream) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String tempString = null;
        boolean readEnd = false;
        do {
            try {
                tempString = reader.readLine();
            } catch (Throwable ignored) {
            }
            if (tempString == null) readEnd = true;
            else stringBuffer.append(tempString);

        } while (!readEnd);
        return stringBuffer.toString();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
