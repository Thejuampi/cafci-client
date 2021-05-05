package com.jpal.cafci.cmd;

import com.jpal.cafci.shared.Args;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.jpal.cafci.shared.Result.error;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgParserTest {

    @Test
    void action_only() {
        assertEquals(
                new Args("action here", Map.of()),
                ArgParser.parse("action here").ok());
    }

    @Test
    void action_and_single_arg() {
        assertEquals(
                new Args("action here", Map.of("arg", "value")),
                ArgParser.parse("action here --arg value").ok());
    }

    @Test
    void action_and_single_char_Arg() {
        assertEquals(
                new Args("action", Map.of("a", "value")),
                ArgParser.parse("action -a value").ok());
    }

    @Test
    void mixin() {
        assertEquals(
                new Args("long action name here", Map.of("longarg", "value1", "a", "value2")),
                ArgParser.parse("long action name here --longarg value1 -a value2").ok());
    }

    @Test
    void errors() {
        assertEquals(error("input is blank"), ArgParser.parse(""));
        assertEquals(error("there are errors in the processing: [cannot split value [a]]"),
                ArgParser.parse("action -a"));
    }
}