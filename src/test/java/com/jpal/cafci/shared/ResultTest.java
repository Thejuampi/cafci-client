package com.jpal.cafci.shared;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void equals_() {
        assertEquals(Result.ok("this is ok"), Result.ok("this is ok"));
        assertNotEquals(Result.ok("this is ok"), Result.error("this is error"));
        assertNotEquals(Result.ok("this is ok"), Result.ok("this is another ok"));

        assertEquals(Result.error("this is error"), Result.error("this is error"));
        assertNotEquals(Result.error("this is an error"), Result.ok("this is ok"));
        assertNotEquals(Result.error("this is an error"), Result.error("this is another error"));

        // lambdas are not comparable
        assertNotEquals(Result.error("formatted %s", "string"), Result.error("formatted %s", "string"));
        assertNotEquals(Result.error("formatted %s", "string"), Result.ok("this is ok"));
        assertNotEquals(Result.error("formatted %s", "string"), Result.error("this is error"));
    }

    @Test
    void enforcements() {
        assertThrows(Exception.class, () -> Result.ok("ok").error());
        assertThrows(Exception.class, () -> Result.error("error").ok());
        assertThrows(Exception.class, () -> Result.error("error %s", "param").ok());

        assertTrue(Result.ok("ok").isOk());
        assertFalse(Result.ok("ok").isError());

        assertTrue(Result.error("error").isError());
        assertFalse(Result.error("error").isOk());

        assertTrue(Result.error("error", 1).isError());
        assertFalse(Result.error("error", 1).isOk());
    }

    @Test
    void matching() {
        var val = new AtomicReference<String>();
        Result.ok("ok").continued(
                val::set,
                err -> fail());
        assertEquals(val.get(), "ok");

        Result.error("error").continued(
                ok -> fail(),
                val::set);
        assertEquals(val.get(), "error");

        Result.error("error %s", "param").continued(
                ok -> fail(),
                val::set);
        assertEquals(val.get(), "error param");
    }
}