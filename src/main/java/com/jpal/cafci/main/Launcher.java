package com.jpal.cafci.main;

import com.jpal.cafci.client.CafciConfig;
import com.jpal.cafci.client.Fund;
import com.jpal.cafci.cmd.Interpreter;
import com.jpal.cafci.shared.Result;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

import static com.jpal.cafci.cmd.ArgsSplitter.split;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toUnmodifiableMap;

// TODO refactor me!
@Log4j2
public class Launcher {

    @SneakyThrows
    public static void main(String... args) {
        log.info("args={}", () -> Arrays.toString(args));

        var config = new CafciConfig();
        var interpreter = new Interpreter(config);

        if (args.length > 0) {
            if (Set.of(args).contains("--file=yield")) {
                printFileYields(interpreter);
            }
        }

        @Cleanup
        var scan = new Scanner(System.in);

        while (true) {
            log.info("waiting for input...");
            System.out.flush();
            var input = scan.nextLine();
            if (input.equals("stop"))
                break;

            if(input.equalsIgnoreCase("fetch")) {
                var funds = config.api()
                        .fetchFunds()
                        .collect(toUnmodifiableMap(
                                Fund::id,
                                fund -> fund));
                config.setAllFundsCommand().set(funds);
                continue;
            }

            try {
                if (input.equals("file"))
                    printFileYields(interpreter);

                print(interpreter.run(split(input)));
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private static void printFileYields(Interpreter interpreter) throws IOException {
        lines(Paths.get("src", "main", "resources", "yields.csv"))
            .filter(l -> !l.startsWith("#"))
            .peek(l -> log.info("line -> {}", l))
            .map(l -> split(l))
            .map(l -> interpreter.run(l))
            .forEach(r -> print(r));
    }

    private synchronized static void print(Result<Stream<String>, String> result) {
        result.continued(message -> message.forEach(log::info), log::error);
    }

}
