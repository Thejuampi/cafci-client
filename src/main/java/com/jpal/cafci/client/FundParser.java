package com.jpal.cafci.client;

import com.jpal.cafci.shared.Pure;

import java.util.Map;

import static com.jpal.cafci.client.Utils.listOfMaps;
import static java.util.stream.Collectors.toList;

public final class FundParser {

    @Pure
    public static Fund parse(Map<?,?> raw) {
        return Fund.builder()
                .id(Utils.string(raw, "id"))
                .name(Utils.string(raw, "nombre"))
                .objective(Utils.string(raw, "objetivo"))
                .classes(FundClassParser.parse(listOfMaps(raw, "clase_fondos")).collect(toList()))
                .build();
    }

}
