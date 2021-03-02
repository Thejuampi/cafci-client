package com.jpal.cafci.client;

import java.util.Map;

public interface SetAllFundsCommand {
    Map<String, Fund> set(Map<String, Fund> funds);
}
