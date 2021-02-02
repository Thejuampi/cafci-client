package com.jpal.cafci.client;

import com.jpal.cafci.shared.Tuple;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Stream.concat;

@UtilityClass
public class YieldReporter {

    public Stream<String> reportYields(Stream<Tuple.Tuple2<FundClass, Yield>> yields) {
        var report = yields
                .map(t -> format("fund: %s yield: %s -> %s",
                        t.t1().name(),
                        t.t2().to(),
                        t.t2().accumulated()));
        return concat(Stream.of("Report of yields", "----------------"), report);
    }

}
