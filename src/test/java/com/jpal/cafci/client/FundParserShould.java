package com.jpal.cafci.client;

import lombok.val;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class FundParserShould {

    @Test
    void parse_fund() {
        Map<?, ?> input = Map.of(
                "id", "1",
                "nombre", "Alianza de Capitales",
                "objetivo", "El objetivo es superar el rendimiento de la tasa BADLAR",
                "clase_fondos", List.of()
        );

        val result = FundParser.parse(input);

        assertThat(result)
                .isEqualTo(Fund.builder()
                        .id("1")
                        .name("Alianza de Capitales")
                        .objective("El objetivo es superar el rendimiento de la tasa BADLAR")
                        .build())
                .hasNoNullFieldsOrProperties();
    }
}