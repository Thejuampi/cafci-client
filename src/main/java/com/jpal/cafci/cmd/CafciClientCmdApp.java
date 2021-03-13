package com.jpal.cafci.cmd;

import com.jpal.cafci.client.*;
import com.jpal.cafci.shared.Impure;
import com.jpal.cafci.shared.Tuple.Tuple2;
import com.jpal.cafci.shared.Utils;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Stream;

import static com.jpal.cafci.shared.Tuple.tuple;
import static java.nio.file.Files.lines;

@Value
@Log4j2
public class CafciClientCmdApp
        implements ActionVisitor {

    CafciConfig config;

    public void run() {
        @Cleanup
        var scan = new Scanner(System.in);

        //noinspection InfiniteLoopStatement
        while (true) {
            log.info("waiting for input...");
            Actions.actionOrError(scan.nextLine())
                    .continued(
                            action -> action.execute(this),
                            log::error
                    );
        }
    }

    @Override
    public void visit(FetchFundsAction ignored) {
        log.info("fetching yields...");
        var funds = config.api().fetchFunds();
        var fundsById = Utils.indexById(funds);
        config.setAllFundsCommand().set(fundsById);
    }

    @SneakyThrows
    @Override
    public void visit(ReadFileAction ignored) {
        var fundAndYields = lines(Paths.get("src", "main", "resources", "yields.csv"))
                .filter(l -> !l.startsWith("#"))
                .map(l -> l.replaceAll("\\s+", "\\\s+"))
                .peek(l -> log.info("line -> {}", l))
                .flatMap(l -> config().fundsQuery().findByClassNameRegex(l))
                .peek(fnc -> log.info("found {}", fnc.t2()))
                .flatMap(fnc -> fetchYields(fnc)
                .map(_yield -> tuple(fnc.t2(), _yield)));

        YieldReporter
                .reportYields(fundAndYields)
                .forEach(log::info);
    }

    @Impure(cause = "LocalDate.now()")
    private Stream<Yield> fetchYields(Tuple2<Fund, FundClass> fnc) {
        return config.api().fetchYield(
                LocalDate.now().minusMonths(1),
                LocalDate.now(),
                fnc.t1(),
                fnc.t2());
    }

}
