package com.jpal.cafci.client;

import com.jpal.cafci.shared.Pure;
import com.jpal.cafci.shared.Tuple;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Stream.concat;

@UtilityClass
public class YieldReporter {

    @Pure
    public Stream<String> reportYields(Stream<Tuple> yields) {
        var report = yields
                .map(t -> format("fund: %s day: %s direct: %.3f%% accumulated: %.3f%%",
                        fundClass(t).name(),
                        day(_yield(t).to()),
                        _yield(t).direct(),
                        _yield(t).accumulated()));
        return concat(Stream.of("Report of yields", "----------------"), report);
    }

    private static String day(LocalDate date) {
        return String.format("%s - %s/%s/%s",
                date.getDayOfWeek().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ROOT),
                date.getDayOfMonth(),
                date.getMonthValue() + 1,
                date.getYear() % 1000);
    }

    private static FundClass fundClass(Tuple t) {
        return t._1();
    }

    private static Yield _yield(Tuple t) {
        return t._2();
    }

}
