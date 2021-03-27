package com.jpal.cafci.client;

import com.jpal.cafci.shared.Tuple;

import java.util.stream.Stream;

public interface FundQuery {

    Stream<Tuple> findByClassNameRegex(String regex);

}
