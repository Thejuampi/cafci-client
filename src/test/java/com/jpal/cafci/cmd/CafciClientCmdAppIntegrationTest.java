package com.jpal.cafci.cmd;

import com.jpal.cafci.client.CafciApi;
import com.jpal.cafci.client.CafciConfig;
import com.jpal.cafci.client.FundRepository;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

//@Disabled("Integration test - end to end")
class CafciClientCmdAppIntegrationTest {

    @NonFinal PipedOutputStream writer;
    @NonFinal PipedInputStream reader;
    @NonFinal FundRepository repo;
    @NonFinal CafciApi api;
    @NonFinal CafciClientCmdApp app;
    @NonFinal Logger log;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        writer = new PipedOutputStream();
        reader = new PipedInputStream();
        reader.connect(writer);
        var config = CafciConfig.create();
        var httpClient = spy(config.httpClient());
        repo = spy(config.repo());
        api = spy(config.api());
        log = mock(Logger.class);
        app = new CafciClientCmdApp(new CafciConfig(repo, httpClient, api), reader, log);
        new Thread(app::run).start();
    }

    @AfterEach
    void tearDown() {
        sendCommand("\n");
        app.stop();
    }

    @Test
    void fetches() {
        // given
        var command = "fetch\n";

        // when
        sendCommand(command);

        // then
        verify(repo, timeout(20_000)).set(anyMap());
    }

    @Test
    void file() {
        fetches();
        var command = "file\n";

        sendCommand(command);

        verify(api, timeout(5000).atLeastOnce()).fetchYield(any(), any(), any(), any());
        verify(log, atLeastOnce()).info("Report of yields");
    }

    @SneakyThrows
    void sendCommand(String command) {
        writer.write(command.getBytes(StandardCharsets.UTF_8));
    }

}