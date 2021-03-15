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

        return error("unable to get an action");
    }

}
