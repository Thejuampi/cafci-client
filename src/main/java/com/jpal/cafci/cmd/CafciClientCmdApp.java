package com.jpal.cafci.cmd;

import com.google.gson.Gson;
import com.jpal.cafci.client.*;
import com.jpal.cafci.shared.Impure;
import com.jpal.cafci.shared.Utils;
import lombok.*;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.spacesAsWildcard;
import static com.jpal.cafci.shared.Tuple.tuple;
import static java.nio.file.Files.lines;

@Value
@Getter(AccessLevel.NONE)
public class CafciClientCmdApp {

    public static final Gson GSON = new Gson();
    AtomicBoolean running = new AtomicBoolean(false);

    CafciConfig config;
    ActionVisitorDelegate actionVisitor = new ActionVisitorDelegate();
    InputStream in;
    Logger log;

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

    private final class ActionVisitorDelegate
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
                    .map(l -> l.replaceAll("\\s+", "\\\s+")).peek(l -> log.info("line -> {}", l))
                    .flatMap(l -> config.fundsQuery().findByClassNameRegex(l)).peek(fnc -> log.info("found {}", fnc::_2))
                    .flatMap(fnc -> fetchYields(fnc._1(), fnc._2(), config.api())
                    .map(_yield -> tuple(fnc._2(), _yield)));

            YieldReporter
                    .reportYields(fundWithYields)
                    .forEach(log::info);
        }

        @Override
        public void visit(FundAction fundAction) {
            var fundsWithYields = config.fundsQuery()
                    .findByClassNameRegex(spacesAsWildcard(fundAction.fund()))
                    .peek(fnc -> log.info("found {}", fnc::_2))
                    .flatMap(fnc -> fetchYields(fnc._1(), fnc._2(), config.api())
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

        @Override
        public void visit(SaveToJsonAction saveToJsonAction) {
            try (val writer = Files.newOutputStream(Paths.get("src", "main", "resources", "funds.json"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                writer.write(bytes("["));
                config.fundsQuery().findAll()
                        .map(fund -> toJson(fund) + ",\n")
                        .map(json -> bytes(json))
                        .forEach(bytes -> write(writer, bytes));
                writer.write(bytes("]"));
            } catch (IOException e) {
                log.error("Error saving funds", e);
            }
        }

        @Impure(cause = "LocalDate.now()")
        private Stream<Yield> fetchYields(Fund fund,
                                          FundClass fundClass,
                                          CafciApi api) {
            return api.fetchYield(
                    LocalDate.now().minusMonths(1),
                    LocalDate.now(),
                    fund,
                    fundClass);
        }
    }

    private byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private void write(OutputStream writer, byte[] bytes) {
        try {
            writer.write(bytes);
        } catch (IOException e) {
            log.error("error writing bytes {}", bytes);
        }
    }

    private String toJson(Fund fund) {
        return GSON.toJson(fund);
    }
}
