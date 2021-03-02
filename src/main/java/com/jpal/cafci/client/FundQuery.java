package com.jpal.cafci.client;

import java.util.stream.Stream;

public interface FundQuery {
    Stream<Fund> values();

    Stream<Fund> findById(String... ids);

    Stream<Fund> findByNameRegex(String... tokens);
}
