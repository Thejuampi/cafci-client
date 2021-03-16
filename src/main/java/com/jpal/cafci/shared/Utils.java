package com.jpal.cafci.shared;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Pure
@UtilityClass
public class Utils {

    @Pure
    public static <K, V> Map<K, V> indexBy(Stream<V> stream, Function<V,K> indexFunc) {
        return stream.collect(toUnmodifiableMap(
                indexFunc,
                v -> v));
    }
}
