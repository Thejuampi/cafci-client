package com.jpal.cafci.cmd;

import com.google.gson.Gson;
import com.jpal.cafci.client.*;
import com.jpal.cafci.shared.Impure;
import com.jpal.cafci.shared.Tuple;
import lombok.*;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.jpal.cafci.client.Utils.spacesAsWildcard;
import static com.jpal.cafci.shared.Tuple.tuple;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;

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
            config.setAllFundsCommand().set(funds);
        }

        @SneakyThrows
        @Override
        public void visit(ReadFileAction ignored) {
            var fundWithYields = lines(Paths.get("src", "main", "resources", "yields.csv"))
                    .filter(line -> !line.startsWith("#"))
                    .map(line -> line.replaceAll("\\s+", "\\\s+"))
                    .flatMap(line -> config.fundsQuery().findByClassNameRegex(line))
                    .flatMap(fundAndClass -> fetchYields(fund(fundAndClass), _class(fundAndClass), config.api())
                    .map(_yield -> tuple(_class(fundAndClass), _yield)));

            YieldReporter
                    .reportYields(fundWithYields)
                    .forEach(log::info);
        }

        private static FundClass _class(Tuple fundAndClass) {
            return fundAndClass._2();
        }

        private static Fund fund(Tuple fundAndClass) {
            return fundAndClass._1();
        }

        @Override
        public void visit(FundAction fundAction) {
            var fundsWithYields = config.fundsQuery()
                    .findByClassNameRegex(spacesAsWildcard(fundAction.fund()))
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
            try (var writer = Files.newOutputStream(Paths.get("src", "main", "resources", "funds.json"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                var json = config.fundsQuery().findAll()
                        .map(fund -> toJson(fund))
                        .collect(joining(",\n", "[", "]"));
                writer.write(bytes(json));
            } catch (IOException e) {
                log.error("Error saving funds", e);
            }
        }

        @Override
        public void visit(ReadFundsFromFileAction __) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get("src", "main", "resources", "funds.json"))) {
                Fund[] funds = new Gson().fromJson(reader, Fund[].class);
                if(funds.length == 0) {
                    log.warn("no funds read from file");
                    return;
                }
                log.info("setting {} new values", funds.length);
                config.setAllFundsCommand().set(Arrays.stream(funds));
            } catch (Exception e) {
                log.error("error reading", e);
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

    private String toJson(Fund fund) {
        return GSON.toJson(fund);
    }
}
