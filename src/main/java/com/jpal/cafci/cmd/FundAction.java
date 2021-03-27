package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Result;
import lombok.Value;

import java.util.Map;

import static com.jpal.cafci.shared.Result.error;
import static com.jpal.cafci.shared.Result.ok;

@Value
public class FundAction implements Action {

    String fund;

    static Result<Action, String> create(Map<String, String> args) {
        if (args.containsKey("name")) return ok(new FundAction(args.get("name")));
        if (args.containsKey("n")) return ok(new FundAction(args.get("n")));

        return error("neither --name nor -n was specified");
    }

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }
}
