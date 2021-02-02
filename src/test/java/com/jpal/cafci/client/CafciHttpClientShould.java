package com.jpal.cafci.client;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;


class CafciHttpClientShould {

    @Test
    void fetch_funds() {
        val client = new CafciHttpClient();

        val result = client.fetchFundJson();

        assertThat(result).isNotBlank();
    }

    @Test
    void fetch_yields() {
        val client = new CafciHttpClient();

        val result = client.rendimiento(
                "1",
                "1",
                LocalDate.of(2021, Month.JANUARY, 1),
                LocalDate.of(2021, Month.JANUARY, 7));

        assertThat(result).isNotEmpty();
    }
}