package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Result;
import com.jpal.cafci.shared.Utils;

import java.util.Map;

import static com.jpal.cafci.shared.Result.error;
import static com.jpal.cafci.shared.Result.ok;

public record FundAction(String fund) implements Action {

    static Result<Action, String> create(Map<String, String> args) {
        return Utils.select(args, "name", "n").findAny()
                .<Result<Action, String>>map(s -> ok(new FundAction(s)))
                .orElseGet(() -> error("neither --name nor -n was specified"));
    }

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }
}
