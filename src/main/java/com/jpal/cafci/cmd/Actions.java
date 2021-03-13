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
        val args = ArgumentParser.parse(rawInput.split(" +"));
        if (args.containsKey("fetch"))
            return ok(new FetchFundsAction());

        if (args.containsKey("file")) {
            return ok(new ReadFileAction());
        }

        return error("unable to get an action");
    }

}
