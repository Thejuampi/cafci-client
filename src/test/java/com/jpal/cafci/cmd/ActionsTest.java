package com.jpal.cafci.cmd;

import org.junit.jupiter.api.Test;

import static com.jpal.cafci.shared.Result.ok;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionsTest {

    @Test
    void file_action() {
        assertEquals(ok(new ReadFileAction()), Actions.action("file"));
    }

    @Test
    void fund_action() {
        assertEquals(ok(new FundAction("arg")), Actions.action("fund --name arg"));
    }

    @Test
    void fetch_action() {
        assertEquals(ok(new FetchFundsAction()), Actions.action("fetch"));
    }

    @Test
    void stop_action() {
        assertEquals(ok(new StopAction()), Actions.action("stop"));
    }
}