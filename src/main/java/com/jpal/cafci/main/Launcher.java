package com.jpal.cafci.main;

import com.jpal.cafci.client.CafciConfig;
import com.jpal.cafci.client.Fund;
import com.jpal.cafci.cmd.Interpreter;
import com.jpal.cafci.shared.Result;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jpal.cafci.cmd.ArgsSplitter.split;
import static java.nio.file.Files.lines;

@Log4j2
public class Launcher {

    @SneakyThrows
    public static void main(String... args) {
        log.info("args={}", () -> Arrays.toString(args));

        var config = new CafciConfig();
        var funds = config.api()
                .fetchFunds()
                .collect(Collectors.toUnmodifiableMap(
                        Fund::id,
                        fund -> fund));
        config.repo().set(funds);
        var interpreter = new Interpreter(config);

        var scan = new Scanner(System.in);

        if (args.length > 0) {
            if (Set.of(args).contains("--file")) {
                printFile(interpreter);
            }
        }

        while (true) {
            log.info("waiting for input...");
            System.out.flush();
            var input = scan.nextLine();
            if (input.equals("stop"))
                break;

            if(input.equals("file"))
                printFile(interpreter);

            try {
                print(interpreter.run(split(input)));
            } catch (Exception e) {
                log.error(e);
            }

        }
    }

    private static void printFile(Interpreter interpreter) throws IOException {
        lines(Paths.get("src", "main", "resources", "input.csv"))
                .peek(l -> log.info("line -> {}", l))
                .map(l -> split(l))
                .map(l -> interpreter.run(l))
                .forEach(r -> print(r));
    }

    private synchronized static void print(Result<Stream<String>, String> result) {
        result.continued(
                message -> message.skip(18).forEach(log::info),
                log::error);
    }

}
