package com.jpal.cafci.client;

import lombok.val;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Collectors.toList;

class CafciApiShould {

    @Test
    void not_fail_when_response_is_empty() {
        CafciHttpClient client = Mockito.mock(CafciHttpClient.class);
        Mockito.when(client.fetchFundJson()).thenReturn("{'success':true, 'data':[]}");

        val result = new CafciApi(client, FundRepository.empty()).fetchFunds();

        Assertions.assertEquals(List.of(), result.collect(toList()));
    }

    @Test
    void return_list_of_funds() {
        val input = readFile("CafciApiShould.return_list_of_funds.json");
        CafciHttpClient client = Mockito.mock(CafciHttpClient.class);
        Mockito.when(client.fetchFundJson()).thenReturn(input);

        val result = new CafciApi(client, FundRepository.empty()).fetchFunds();

        Assertions.assertEquals(List.of(Fund.builder()
                .id("fund-id")
                .name("fund-name")
                .objective("objective-description")
                .classes(List.of(FundClass.builder()
                        .name("fund-class-name")
                        .id("fund-class-id")
                        .build()))
                .build()), result.collect(toList()));
    }

    private String readFile(String filename) {
        return Files.contentOf(new File("src/test/resources/" + filename),
                StandardCharsets.UTF_8);
    }
}
