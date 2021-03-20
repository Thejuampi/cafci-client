package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Args;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.jpal.cafci.shared.Result.error;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgParserV2Test {

    @Test
    void action_only() {
        assertEquals(
                new Args("action here", Map.of()),
                ArgParserV2.parse("action here").ok());
    }

    @Test
    void action_and_single_arg() {
        assertEquals(
                new Args("action here", Map.of("arg", "value")),
                ArgParserV2.parse("action here --arg value").ok());
    }

    @Test
    void action_and_single_char_Arg() {
        assertEquals(
                new Args("action", Map.of("a", "value")),
                ArgParserV2.parse("action -a value").ok());
    }

    @Test
    void mixin() {
        assertEquals(
                new Args("long action name here", Map.of("longarg", "value1", "a", "value2")),
                ArgParserV2.parse("long action name here --longarg value1 -a value2").ok());
    }

    @Test
    void errors() {
        assertEquals(error("input is blank"), ArgParserV2.parse(""));
        assertEquals(error("there are errors in the processing: [cannot split value [a]]"),
                ArgParserV2.parse("action -a"));
    }
}