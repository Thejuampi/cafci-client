package com.jpal.cafci.cmd;

import lombok.val;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class ArgumentParser {

    /**
     * @return map e.g: {funds:5, classes:7} or {funds:""}
     */
    public static Map<String, String> parse(String[] args) {
        if(args.length == 1)
            return Map.of(args[0], "");

        if(args.length == 2)
            return Map.of(args[0], args[1]);

        Map<String, String> builder = new HashMap<>();
        for (int i = 0; i < args.length; i+=2) {
            val key = args[i];
            val value = emptyIfBlack(args[i + 1]);

            if(key == null || key.isBlank())
                continue;

            builder.put(key, value);
        }

        return unmodifiableMap(builder);
    }

    private static String emptyIfBlack(String arg) {
        return (arg == null || arg.isBlank()) ?
                "" :
                arg;
    }
}
