package com.jpal.cafci.shared;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsTest {

    @Test
    void instantiation_is_not_allowed() {
        assertThrows(Exception.class, () -> Utils.class.getDeclaredConstructor().newInstance()) ;
    }

    @Test
    void indexBy() {
        assertEquals(
                Map.of(1, List.of(1, 2)),
                Utils.indexBy(Stream.of(List.of(1, 2)), l -> l.get(0)));

    }
}