package org.crud2.util;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {
    public static Integer toInt(String s) {
        if (s == null || s.length() == 0) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static Date toDate(String s, String format) {
        if (s == null || s.length() == 0) return null;
        try {
            Format f = new SimpleDateFormat(format);
            return (Date) f.parseObject(s);
        } catch (ParseException ignored) {
            return null;
        }
    }

    public static Date toDate(String s) {
        if (s == null || s.length() == 0) return null;
        else if (s.length() <= 10)
            return toDate(s, "yyyy-MM-dd");
        else return toDateTime(s);
    }

    public static Date toDateTime(String s) {
        return toDate(s, "yyyy-MM-dd HH:mm:ss");
    }

    public static BigDecimal toDecimal(String s) {
        if (s == null || s.length() == 0) return null;
        return new BigDecimal(s);
    }
}
