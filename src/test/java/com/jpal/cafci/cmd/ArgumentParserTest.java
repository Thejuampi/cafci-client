package com.jpal.cafci.cmd;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgumentParserTest {

    @Test
    void single_arg() {
        assertEquals(
                Map.of("arg", ""),
                ArgumentParser.parse(new String[]{"arg"}));
    }

    @Test
    void two_arg_and_value() {
        assertEquals(
                Map.of("arg", "val"),
                ArgumentParser.parse(new String[]{"arg", "val"}));
    }

    @Test
    void many_args() {
        assertEquals(
                Map.of("arg0", "val0", "arg1", "val1"),
                ArgumentParser.parse(new String[]{"arg0", "val0", "arg1", "val1"}));
    }
}