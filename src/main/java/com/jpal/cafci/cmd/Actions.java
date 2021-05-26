package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Pure;
import com.jpal.cafci.shared.Result;
import lombok.experimental.UtilityClass;

import static com.jpal.cafci.shared.Result.error;
import static com.jpal.cafci.shared.Result.ok;

@UtilityClass
public class Actions {

    @Pure
    Result<Action, String> action(String input) {
        var actionAndArgs = ArgParser.parse(input);
        if (actionAndArgs.isError()) return actionAndArgs.cast();

        var args = actionAndArgs.ok();
        return switch (args.action()) {
            case "fetch" -> ok(FetchFundsAction.instance());
            case "file" -> ok(ReadFileAction.instance());
            case "fund" -> FundAction.create(args.args());
            case "stop" -> ok(StopAction.instance());
            case "save" -> ok(new SaveToJsonAction());
            case "read" -> ok(new ReadFundsFromFileAction());
            default -> error("unable to get an action for input [%s]", input);
        };
    }

}
