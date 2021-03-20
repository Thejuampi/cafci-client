package com.jpal.cafci.client;

import com.jpal.cafci.shared.Pure;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.string;

public class FundClassParser {

    @Pure
    public static Stream<FundClass> parse(List<Map<?,?>> raw) {
        return raw.stream()
                .map(FundClassParser::parse);
    }

    @Pure
    private static FundClass parse(Map<?, ?> m) {
        return FundClass.builder()
                .id(string(m, "id"))
                .name(string(m, "nombre"))
                .build();
    }
}
