package com.jpal.cafci.shared;

import com.jpal.cafci.client.Fund;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Pure
@UtilityClass
public class Utils {

    public static Map<String, Fund> indexById(Stream<Fund> funds) {
        return funds.collect(toUnmodifiableMap(
                Fund::id,
                fund -> fund));
    }
}
