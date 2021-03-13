package com.jpal.cafci.main;

import com.jpal.cafci.client.CafciConfig;
import com.jpal.cafci.cmd.CafciClientCmdApp;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

// TODO refactor me!
@Log4j2
public class Launcher {

    @SneakyThrows
    public static void main(String... args) {
        log.info("args={}", () -> Arrays.toString(args));
        var config = new CafciConfig();

        new CafciClientCmdApp(config).run();
    }

}
