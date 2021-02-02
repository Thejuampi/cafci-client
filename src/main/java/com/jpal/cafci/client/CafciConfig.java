package com.jpal.cafci.client;

import lombok.Value;

@Value
public class CafciConfig {

    FundRepository repo;
    CafciHttpClient httpClient;
    CafciApi api;

    public CafciConfig() {
        repo = FundRepository.empty();
        httpClient = new CafciHttpClient();
        api = new CafciApi(httpClient, repo);
    }

}
