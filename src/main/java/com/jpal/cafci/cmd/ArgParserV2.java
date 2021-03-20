package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Args;
import com.jpal.cafci.shared.Result;

import java.util.Arrays;
import java.util.Map;

import static com.jpal.cafci.shared.Result.error;
import static com.jpal.cafci.shared.Result.ok;
import static java.util.stream.Collectors.*;

public class ArgParserV2 {

    public static Result<Args, String> parse(String input) {
        if (input.isBlank())
            return error("input is blank");

        var tokens = input.split("-{1,2}");
        var action = tokens[0].trim();
        if (tokens.length == 1)
            return ok(new Args(action, Map.of()));

        var argsOrErrors = Arrays.stream(tokens)
                .skip(1)
                .map(token -> token.split("\\s+"))
                .map(anv -> anv.length == 2 ? _ok(anv) : _error(anv))
                .collect(groupingBy(Result::isError));

        if (argsOrErrors.containsKey(true)) // there are errors
            return error("there are errors in the processing: "
                    + argsOrErrors.get(true).stream()
                    .map(r -> r.error())
                    .collect(joining(", ", "[", "]")));

        var args = argsOrErrors.get(false).stream()
                .map(anv -> anv.ok())
                .collect(toUnmodifiableMap(
                        anv -> anv[0],
                        anv -> anv[1]));

        return ok(new Args(action, args));
    }

    private static Result<String[], String> _ok(String[] anv) {
        return Result.ok(anv);
    }

    private static Result<String[], String> _error(String[] anv) {
        return Result.error("cannot split value " + Arrays.toString(anv));
    }

}
