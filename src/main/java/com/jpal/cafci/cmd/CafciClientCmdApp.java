package com.jpal.cafci.cmd;

import com.jpal.cafci.client.*;
import com.jpal.cafci.shared.Impure;
import com.jpal.cafci.shared.Utils;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.spacesAsWildcard;
import static com.jpal.cafci.shared.Tuple.tuple;
import static java.nio.file.Files.lines;

@Value
@Log4j2
public class CafciClientCmdApp {

    AtomicBoolean running = new AtomicBoolean(false);

    CafciConfig config;
    ActionVisitorDelegate actionVisitor = new ActionVisitorDelegate();
    InputStream in;

    public void run() {
        running.set(true);
        @Cleanup
        var scan = new Scanner(in);

        while (running.get()) {
            log.info("waiting for input...");
            Actions.action(scan.nextLine())
                    .continued(
                            action -> action.execute(actionVisitor),
                            log::error
                    );
        }
    }

    public void stop() {
        this.running.set(false);
    }

    @Impure(cause = "LocalDate.now()")
    private Stream<Yield> fetchYields(Fund fund,
                                      FundClass fundClass) {
        return config.api().fetchYield(
                LocalDate.now().minusMonths(1),
                LocalDate.now(),
                fund,
                fundClass);
    }

    private class ActionVisitorDelegate
            implements ActionVisitor {

        @Override
        public void visit(FetchFundsAction ignored) {
            log.info("fetching yields...");
            var funds = config.api().fetchFunds();
            var fundsById = Utils.indexBy(funds, Fund::id);
            config.setAllFundsCommand().set(fundsById);
        }

        @SneakyThrows
        @Override
        public void visit(ReadFileAction ignored) {
            var fundWithYields = lines(Paths.get("src", "main", "resources", "yields.csv"))
                    .filter(l -> !l.startsWith("#"))
                    .map(l -> l.replaceAll("\\s+", "\\\s+"))
                    .peek(l -> log.info("line -> {}", l))
                    .flatMap(l -> config().fundsQuery().findByClassNameRegex(l))
                    .peek(fnc -> log.info("found {}", fnc::_2))
                    .flatMap(fnc -> fetchYields(fnc._1(), fnc._2())
                            .map(_yield -> tuple(fnc._2(), _yield)));

            YieldReporter
                    .reportYields(fundWithYields)
                    .forEach(log::info);
        }

        @Override
        public void visit(FundAction fundAction) {
            var fundsWithYields = config().fundsQuery()
                    .findByClassNameRegex(spacesAsWildcard(fundAction.fund()))
                    .peek(fnc -> log.info("found {}", () -> fnc._2()))
                    .flatMap(fnc -> fetchYields(fnc._1(), fnc._2())
                            .map(_yield -> tuple(fnc._2(), _yield)));

            YieldReporter.reportYields(fundsWithYields)
                    .forEach(log::info);
        }

        @Override
        @Impure(cause = "sets running flag to false")
        public void visit(StopAction __) {
            log.info("stopping...");
            stop();
        }
    }
}
