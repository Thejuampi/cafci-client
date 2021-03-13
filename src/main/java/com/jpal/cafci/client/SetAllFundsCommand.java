package com.jpal.cafci.client;

import com.jpal.cafci.shared.Impure;

import java.util.Map;

public interface SetAllFundsCommand {
    @Impure(cause = "may mutate state")
    Map<String, Fund> set(Map<String, Fund> funds);
}
