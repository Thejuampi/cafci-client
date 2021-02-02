package com.jpal.cafci.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FundClassParser {

    public static Stream<FundClass> parse(List<Map<?,?>> raw) {
        return raw.stream()
                .map(FundClassParser::parse);
    }

    private static FundClass parse(Map<?, ?> m) {
        return FundClass.builder()
                .id(Utils.string(m, "id"))
                .name(Utils.string(m, "nombre"))
                .build();
    }
}
