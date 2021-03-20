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
    @Deprecated
    Result<Action, String> actionOrError(String rawInput) {
        val args = ArgumentParser.parse(ArgsSplitter.split(rawInput));
        if (args.containsKey("fetch"))
            return ok(new FetchFundsAction());

        if (args.containsKey("file")) {
            return ok(new ReadFileAction());
        }

        if(args.containsKey("fund")) {
            if(args.get("fund") == null) return error("no fund provided");

            return ok(new FundAction(args.get("fund")));
        }

        if(args.containsKey("stop"))
            return ok(new StopAction());

        return error("unable to get an action");
    }

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
