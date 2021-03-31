package com.jpal.cafci.main;

import com.jpal.cafci.client.CafciConfig;
import com.jpal.cafci.cmd.CafciClientCmdApp;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.Arrays;

@Log4j2
public class Launcher {

    @SneakyThrows
    public static void main(String... args) {
        log.info("args={}", () -> Arrays.toString(args));
        var config = CafciConfig.create();

        new CafciClientCmdApp(
                config,
                System.in,
                LogManager.getLogger(CafciClientCmdApp.class))
                .run();
    }

}
