package com.jpal.cafci.client;

import com.jpal.cafci.shared.Pure;
import com.jpal.cafci.shared.Tuple.Tuple2;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Stream.concat;

@UtilityClass
public class YieldReporter {

    @Pure
    public Stream<String> reportYields(Stream<Tuple2<FundClass, Yield>> yields) {
        var report = yields
                .map(t -> format("fund: %s day: %s direct: %.3f%% accumulated: %.3f%%",
                        fundClass(t).name(),
                        _yield(t).to(),
                        _yield(t).direct(),
                        _yield(t).accumulated()));
        return concat(Stream.of("Report of yields", "----------------"), report);
    }

    private static FundClass fundClass(Tuple2<FundClass, Yield> t) {
        return t.t1();
    }

    private static Yield _yield(Tuple2<FundClass, Yield> t) {
        return t.t2();
    }

}
