package com.jpal.cafci.client;

import com.jpal.cafci.shared.Impure;
import com.jpal.cafci.shared.Pure;
import com.jpal.cafci.shared.Tuple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.containing;
import static com.jpal.cafci.shared.Tuple.tuple;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@Value
@Log4j2
public class FundRepository
        implements SetAllFundsCommand, FundQuery {

    @Getter(AccessLevel.PACKAGE)
    public AtomicReference<Map<String, Fund>> cache;

    @Pure
    public static FundRepository empty() {
        return withInitialValue(Map.of());
    }

    @Pure
    public static FundRepository withInitialValue(Map<String, Fund> initialValue) {
        return new FundRepository(initialValue);
    }

    private FundRepository(Map<String, Fund> initialValue) {
        cache = new AtomicReference<>(Map.copyOf(initialValue));
    }

    @Override
    @Impure(cause = "state mutation and logging")
    public Map<String, Fund> set(Map<String, Fund> funds) {
        log.info("setting {} new values", funds.size());
        return cache.getAndSet(Map.copyOf(funds));
    }

    @Override
    @Impure(cause = "output may change from call to call")
    public Stream<Tuple> findByClassNameRegex(String regex) {
        var pattern = compile(containing(regex), CASE_INSENSITIVE);
        return cache.get().values().stream()
                .flatMap(fund -> fund.classes().stream()
                .filter(fundClass -> pattern
                        .matcher(fundClass.name())
                        .matches())
                .map(fundClass -> tuple(fund, fundClass)));
    }

    @Override
    public Stream<Fund> findAll() {
        return cache.get().values().stream();
    }
}
