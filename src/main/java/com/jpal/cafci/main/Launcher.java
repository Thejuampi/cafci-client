package com.jpal.cafci.main;

import com.jpal.cafci.client.CafciConfig;
import com.jpal.cafci.client.Fund;
import com.jpal.cafci.cmd.Interpreter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.jpal.cafci.cmd.ArgsSplitter.split;

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

        while(true) {
            String input = scan.nextLine();
            if(input.equals("stop"))
                break;

            try {
                val result = interpreter.run(split(input));
                result.continued(log::info, log::error);
            } catch (Exception e) {
                log.error(e);
            }

        }
    }

}
