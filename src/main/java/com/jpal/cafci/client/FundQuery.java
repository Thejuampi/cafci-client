package com.jpal.cafci.client;

import com.jpal.cafci.shared.Tuple;

import java.util.Collection;
import java.util.stream.Stream;

public interface FundQuery {

    Stream<Tuple> findByClassNameRegex(String regex);

    Stream<Fund> findAll();

}
