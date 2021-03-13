package com.jpal.cafci.client;

import com.jpal.cafci.shared.Pure;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.*;

@UtilityClass
public class YieldParser {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Pure
    Stream<Yield> parse(List<Map<?,?>> rawList) {
        return rawList.stream()
                .map(YieldParser::parse);
    }

    @Pure
    Yield parse(Map<?, ?> raw) {
        return Yield.builder()
                .from(date(map(raw, "desde"), "fecha", fmt))
                .to(date(map(raw, "hasta"), "fecha", fmt))
                .value(number(map(raw, "hasta"), "valor"))
                .accumulated(number(raw, "rendimiento"))
                .direct(number(raw, "directo"))
                .tna(number(raw, "tna"))
                .build()
                ;
    }

}
