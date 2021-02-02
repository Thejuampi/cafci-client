package com.jpal.cafci.client;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@Value
@Log4j2
public class FundRepository {

    AtomicReference<Map<String, Fund>> cache;

    public static FundRepository empty() {
        return withInitialValue(Map.of());
    }

    public static FundRepository withInitialValue(Map<String, Fund> initialValue) {
        return new FundRepository(initialValue);
    }

    private FundRepository(Map<String, Fund> initialValue) {
        cache = new AtomicReference<>(Map.copyOf(initialValue));
    }

    /**
     * @returns previous values
     */
    public Map<String, Fund> set(Map<String, Fund> funds) {
        log.info("setting new values");
        return cache.getAndSet(Map.copyOf(funds));
    }

    public Stream<Fund> values() {
        return cache.get().values().stream();
    }

    public Stream<Fund> findById(String... ids) {
        val current = cache.get();
        return stream(ids).distinct()
                .map(current::get)
                .filter(Objects::nonNull);
    }

    public Stream<Fund> findByNameRegex(String... tokens) {
        val current = cache.get().values();

        return stream(tokens)
                .map(token -> Utils.containing(token))
                .map(token -> Pattern.compile(token, Pattern.CASE_INSENSITIVE))
                .flatMap(pattern -> current.stream()
                .filter(fund -> pattern.matcher(fund.name()).matches()));
    }

}
