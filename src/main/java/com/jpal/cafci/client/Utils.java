package com.jpal.cafci.client;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Utils {

    public static String string(Map<?, ?> raw, String field) {
        return (String) raw.get(field);
    }

    public double number(Map<?,?> raw, String field) {
        val o = raw.get(field);
        if(o instanceof String)
            return Double.parseDouble((String) o);

        return (double) o;
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    public static List<Map<?, ?>> listOfMaps(Map<?, ?> raw, String field) {
        List<Map<?, ?>> result = (List<Map<?, ?>>) raw.get(field);
        if (result == null) return Collections.emptyList();
        return result;
    }

    public static Boolean bool(Map<?, ?> map, String field) {
        return (Boolean) map.get(field);
    }

    public static void checkArgument(boolean condition, String message) {
        if(!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    Map<?,?> map(Map<?,?> raw, String field) {
        return (Map<?, ?>) raw.get(field);
    }

    static LocalDate date(Map<?, ?> raw, String field, DateTimeFormatter fmt) {
        return LocalDate.parse(string(raw, field), fmt);
    }

    public static String containing(String token) {
        var start = token.startsWith(".*") ? "" : ".*";
        var end = token.endsWith(".*") ? "" : ".*";

        return start + token + end;
    }
}
