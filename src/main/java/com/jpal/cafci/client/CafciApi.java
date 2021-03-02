package com.jpal.cafci.client;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.*;
import static java.lang.Boolean.TRUE;

@ExtensionMethod({Utils.class})
@RequiredArgsConstructor
public class CafciApi {

    private final Gson gson = new Gson();

    @NonNull
    private final CafciHttpClient client;
    @NonNull
    private final SetAllFundsCommand repository;

    @SuppressWarnings("unchecked")
    public Stream<Fund> fetchFunds() {
        val json = client.fetchFundJson();
        Map<?, ?> map = gson.fromJson(json, Map.class);
        enforceSuccess(map);

        val data = (List<Map<?, ?>>) map.get("data");

        return data.stream().map(FundParser::parse);
    }

    public Stream<Yield> fetchYield(LocalDate from, LocalDate to, Fund fund, FundClass fundClass) {
        val fundYieldJson = client.rendimiento(fund.id(), fundClass.id(), from, to);
        Map<?, ?> raw = gson.fromJson(fundYieldJson, Map.class);

        if (raw.bool("success") != TRUE)
            return Stream.empty();

        return YieldParser.parse(raw.listOfMaps("data"));
    }

    private void enforceSuccess(Map<?, ?> map) {
        if (bool(map, "success") != TRUE) {
            throw new IllegalStateException("failed to fetch funds: " + map);
        }
    }

}
