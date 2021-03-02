package com.jpal.cafci.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
public class CafciConfig {

    @Getter(AccessLevel.NONE)
    FundRepository repo;
    CafciHttpClient httpClient;
    CafciApi api;

    public CafciConfig() {
        repo = FundRepository.empty();
        httpClient = new CafciHttpClient();
        api = new CafciApi(httpClient, repo);
    }

    public final FundQuery fundsQuery() {
        return repo;
    }

    public final SetAllFundsCommand setAllFundsCommand() {
        return repo;
    }



}
