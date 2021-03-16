package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Result;
import org.junit.jupiter.api.Test;

import static com.jpal.cafci.shared.Result.ok;
import static org.junit.jupiter.api.Assertions.*;

class ActionsTest {

    @Test
    void file_action() {
        assertEquals(ok(new ReadFileAction()), Actions.actionOrError("file"));
    }

    @Test
    void fund_action() {
        assertEquals(ok(new FundAction("arg")), Actions.actionOrError("fund arg"));
    }

    @Test
    void fetch_action() {
        assertEquals(ok(new FetchFundsAction()), Actions.actionOrError("fetch"));
    }
}