package com.jpal.cafci.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
public class CafciConfig {

    FundRepository repo;
    CafciHttpClient httpClient;
    CafciApi api;

    public static CafciConfig create() {
        var repo = FundRepository.empty();
        var httpClient = new CafciHttpClient();
        var api = new CafciApi(httpClient, repo);

        return new CafciConfig(repo, httpClient, api);
    }

    public final FundQuery fundsQuery() {
        return repo;
    }

    public final SetAllFundsCommand setAllFundsCommand() {
        return repo;
    }



}
