package com.jpal.cafci.cmd;

import lombok.val;

public class ArgsSplitter {

    public static String[] split(String input) {
        if (input.isBlank()) {
            return new String[0];
        }

        val result = input
                .replaceAll("'", "\"")
                .split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].replaceAll("[\"|']", "");
        }

        return result;
    }

}
