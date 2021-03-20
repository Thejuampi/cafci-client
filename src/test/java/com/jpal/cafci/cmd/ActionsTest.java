package com.jpal.cafci.cmd;

import org.junit.jupiter.api.Test;

import static com.jpal.cafci.shared.Result.ok;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionsTest {

    @Test
    void file_action() {
        assertEquals(ok(new ReadFileAction()), Actions.v2("file"));
    }

    @Test
    void fund_action() {
        assertEquals(ok(new FundAction("arg")), Actions.v2("fund --name arg"));
    }

    @Test
    void fetch_action() {
        assertEquals(ok(new FetchFundsAction()), Actions.v2("fetch"));
    }
}