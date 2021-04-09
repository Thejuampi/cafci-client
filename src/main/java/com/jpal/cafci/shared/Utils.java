package com.jpal.cafci.shared;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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

    @Pure
    public static <K, V> Stream<V> select(Map<K, V> m, K... keys) {
        return Arrays.stream(keys)
                .map(m::get)
                .filter(Objects::nonNull);
    }

}
