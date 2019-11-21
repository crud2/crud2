package org.crud2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {
    public static Logger logger = LoggerFactory.getLogger(Convert.class);

    public static Integer toInt(String s) {
        if (s == null || s.length() == 0) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static Integer toInt(Object o) {
        if (o instanceof Integer) return (Integer) o;
        else if (o instanceof String) {
            return toInt((String) o)
        }
        logger.error(String.format("can not convert %s to Integer", o.getClass()));
        return null;
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

    public static Date toDate(Object o) {
        if (o instanceof Date) return (Date) o;
        else if (o instanceof String) {
            return toDate((String) o);
        }
        logger.error(String.format("can not convert %s to Date", o.getClass()));
        return null;
    }

    public static Date toDateTime(String s) {
        return toDate(s, "yyyy-MM-dd HH:mm:ss");
    }

    public static BigDecimal toDecimal(Object o) {
        if (o == null) return null;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        else if (o instanceof String) {
            return new BigDecimal((String) o);
        }
        logger.error(String.format("can not convert %s to BigDecimal", o.getClass()));
        return null;
    }
}
