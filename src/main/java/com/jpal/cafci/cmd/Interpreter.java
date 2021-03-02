package com.jpal.cafci.cmd;

import com.jpal.cafci.client.*;
import com.jpal.cafci.shared.Result;
import com.jpal.cafci.shared.Tuple;
import lombok.val;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.containing;
import static com.jpal.cafci.shared.Result.error;
import static com.jpal.cafci.shared.Result.ok;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Comparator.comparing;

public class Interpreter {

    private final CafciConfig config;

    public Interpreter(CafciConfig config) {
        this.config = config;
    }

    public Result<Stream<String>, String> run(String[] rawArgs) {
        if(rawArgs == null || rawArgs.length <= 0) return error("rawArgs is empty or null");

        val args = ArgumentParser.parse(rawArgs);
        if(!args.containsKey("funds")) return error(format("'funds' key is not present in the arguments [%s]", args));

        try {
            if(args.containsKey("yield"))
                if(args.containsKey("from") && args.containsKey("to"))
                    return ok(YieldReporter.reportYields(findYields(
                            args.get("funds"),
                            args.get("from"),
                            args.get("to"),
                            args.get("classes"))));
                else if(args.containsKey("lastMonth")){
                    return ok(YieldReporter.reportYields(findYields(args.get("funds"),
                            args.get("classes"),
                            LocalDate.now().minusMonths(1),
                            LocalDate.now())));
                } else if(args.containsKey("today")) {
                    return ok(YieldReporter.reportYields(findYields(args.get("funds"),
                            args.get("classes"),
                            previousWorkingDay(),
                            LocalDate.now())));
                }
                else
                    return error("from yyyyMMdd and to yyyyMMdd are not present");

            return ok(report(findFunds(args.get("funds"))));
        } catch (Exception e) {
            return error("error: " + e.getMessage());
        }
    }

    private LocalDate previousWorkingDay() {
        var yesterday = LocalDate.now().minusDays(1);

        if(yesterday.getDayOfWeek() == DayOfWeek.SUNDAY)
            return yesterday.minusDays(2);

        if(yesterday.getDayOfWeek() == DayOfWeek.SATURDAY)
            return yesterday.minusDays(1);

        return yesterday;
    }

    private Stream<Tuple.Tuple2<FundClass, Yield>> findYields(String funds,
                                                              String from,
                                                              String to,
                                                              String classes) {
        val fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        return findYields(funds, classes, LocalDate.parse(from, fmt), LocalDate.parse(to, fmt));
    }

    private Stream<Tuple.Tuple2<FundClass, Yield>> findYields(String funds,
                                                              String classes,
                                                              LocalDate fromDate,
                                                              LocalDate toDate) {
        Predicate<FundClass> p = getFundClassPredicate(classes);
        return findFunds(funds)
                .flatMap(fund -> fund.classes().stream()
                .filter(p)
                .flatMap(fundClass -> fetchYields(fund, fundClass, fromDate, toDate)
                .map(_yield -> Tuple.tuple(fundClass, _yield))));
    }

    private Predicate<FundClass> getFundClassPredicate(String classes) {
        var pattern = classes == null ?
                null :
                Pattern.compile(containing(classes), Pattern.CASE_INSENSITIVE);

        Predicate<FundClass> predicate = pattern == null ?
                f -> true :
                f -> pattern.matcher(f.name()).matches();

        return predicate;
    }

    private Stream<Yield> fetchYields(Fund fund, FundClass fundClass, LocalDate from, LocalDate to) {
        return config.api().fetchYield(from, to, fund, fundClass);
    }

    // this may return Result<Stream<Fund>, String>
    private Stream<Fund> findFunds(String funds) {
        if(funds == null || funds.isBlank()) {
            return config.fundsQuery().values();
        }

        val fundTokens = funds.split(",");
        if(fundTokens[0].matches("^\\d+$")) {
            return config.fundsQuery().findById(funds.split(","));
        }

        if(fundTokens[0].matches("(\\w+.*)+")) {
            return config.fundsQuery().findByNameRegex(fundTokens);
        }

        return Stream.empty();
    }

    public static Stream<String> report(Stream<Fund> funds) {
        return funds
                .sorted(comparing(fund -> parseInt(fund.id())))
                .map(Fund::toString);
    }

}
