package com.jpal.cafci.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    @Test
    void equals_() {
        assertEquals(Tuple.tuple(1), Tuple.tuple(1));
        assertNotEquals(Tuple.tuple(1), Tuple.tuple(2));
        assertNotEquals(Tuple.tuple(1), Tuple.tuple(1, 2));

        assertEquals(Tuple.tuple(1, 2), Tuple.tuple(1, 2));
        assertNotEquals(Tuple.tuple(1, 2), Tuple.tuple(2, 1));
        assertNotEquals(Tuple.tuple(1, 2), Tuple.tuple(2));
    }

    @Test
    void accessors() {
        assertEquals("a", Tuple.tuple("a")._1());
        assertThrows(Exception.class, () -> Tuple.tuple(1)._2());

        assertEquals("a", Tuple.tuple("a", "b")._1());
        assertEquals("b", Tuple.tuple("a", "b")._2());
    }
}