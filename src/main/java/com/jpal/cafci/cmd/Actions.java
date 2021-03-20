package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Pure;
import com.jpal.cafci.shared.Result;
import lombok.experimental.UtilityClass;
import lombok.val;

import static com.jpal.cafci.shared.Result.error;
import static com.jpal.cafci.shared.Result.ok;

@UtilityClass
public class Actions {

    @Pure
    Result<Action, String> v2(String input) {
        var actionAndArgs = ArgParserV2.parse(input);
        if(actionAndArgs.isError()) return error(actionAndArgs.error());

        switch (actionAndArgs.ok().action()) {
            case "fetch": return ok(new FetchFundsAction());
            case "file": return ok(new ReadFileAction());
            case "fund": {
                var args = actionAndArgs.ok().args();

                if (args.containsKey("name")) return ok(new FundAction(args.get("name")));
                if (args.containsKey("n")) return ok(new FundAction(args.get("n")));
                return error("neither --name nor -n was specified");
            }
            default:
                return error("unable to get an action");
        }
    }

}
