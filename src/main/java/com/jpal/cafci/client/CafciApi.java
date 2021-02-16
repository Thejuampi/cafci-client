package com.jpal.cafci.client;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.*;
import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
public class CafciApi {

    private final Gson gson = new Gson();

    @NonNull
    private final CafciHttpClient client;
    @NonNull
    private final FundRepository repository;

    @SuppressWarnings("unchecked")
    public Stream<Fund> fetchFunds() {
        val json = client.fetchFundJson();
        Map<?, ?> map = gson.fromJson(json, Map.class);
        enforceSuccess(map);

        val data = (List<Map<?, ?>>) map.get("data");

        return data.stream().map(FundParser::parse);
    }

    public Stream<Yield> fetchYield(LocalDate from, LocalDate to, Fund fund, FundClass fundClass) {
        val fundYield = client.rendimiento(fund.id(), fundClass.id(), from, to);
        Map<?, ?> map = gson.fromJson(fundYield, Map.class);

        if (bool(map, "success") != TRUE)
            return Stream.empty();

        return YieldParser.parse(listOfMaps(map, "data"));
    }

    private void enforceSuccess(Map<?, ?> map) {
        if (bool(map, "success") != TRUE) {
            throw new IllegalStateException("failed to fetch funds: " + map);
        }
    }

}
