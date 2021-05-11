package com.jpal.cafci.client;

import com.jpal.cafci.shared.Impure;

import java.util.Map;
import java.util.stream.Stream;

public interface SetAllFundsCommand {
    @Impure(cause = "may mutate state")
    Map<String, Fund> set(Map<String, Fund> funds);
    @Impure(cause = "may mutate state")
    Map<String, Fund> set(Stream<Fund> funds);
}
