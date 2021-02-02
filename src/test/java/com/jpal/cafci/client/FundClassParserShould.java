package com.jpal.cafci.client;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;


class FundClassParserShould {

    @Test
    void parse_fund_class() {
        Map<?, ?> input = Map.of("id", "1",
                "nombre", "Alianza de Capitales");

        val result = FundClassParser.parse(List.of(input))
                .collect(toList());

        assertThat(result).isEqualTo(List.of(FundClass.builder()
                .id("1")
                .name("Alianza de Capitales")
                .build()));
    }
}