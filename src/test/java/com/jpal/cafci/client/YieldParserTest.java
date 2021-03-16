package com.jpal.cafci.client;

import com.google.gson.Gson;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static com.jpal.cafci.client.Utils.map;

class YieldParserTest {
    @Test
    void parses_map() {
        val json = "{\"success\":true,\"data\":{\"desde\":{\"fecha\":\"04/01/2021\",\"valor\":\"3322.764\"},\"hasta\":{\"fecha\":\"29/01/2021\",\"valor\":3418.589},\"rendimiento\":\"2.8839\",\"tna\":\"42.1049\",\"directo\":\"2.8839\"}}\n";

        Gson gson = new Gson();
        Map<?,?> map = gson.fromJson(json, Map.class);

        val result = YieldParser.parse(List.of(map(map, "data"))).findAny();

        Assertions.assertThat(result).get().isEqualTo(Yield.builder()
                .from(LocalDate.of(2021, Month.JANUARY, 4))
                .to(LocalDate.of(2021, Month.JANUARY, 29))
                .value(3418.589)
                .direct(2.8839)
                .accumulated(2.8839)
                .tna(42.1049)
                .build());

    }
}