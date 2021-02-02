package com.jpal.cafci.cmd;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArgsSplitterTest {

    @Test
    void empty() {
        val result = ArgsSplitter.split("");

        assertThat(result).isEmpty();
    }

    @Test
    void funds() {
        val result = ArgsSplitter.split("funds");

        assertThat(result).containsExactly("funds");
    }

    @Test
    void funds_something() {
        val result = ArgsSplitter.split("funds something");

        assertThat(result)
                .containsExactly("funds", "something");
    }

    @Test
    void funds_something_space_another() {
        val result = ArgsSplitter.split("funds \"something another\"");

        assertThat(result)
                .containsExactly("funds", "something another");
    }

    @Test
    void funds_something_space_another_single_quotes() {
        val result = ArgsSplitter.split("funds 'something another'");

        assertThat(result)
                .containsExactly("funds", "something another");
    }

    @Test
    void funds_something_space_another_classes() {
        val result = ArgsSplitter.split("funds \"something another\" classes");

        assertThat(result)
                .containsExactly("funds", "something another", "classes");
    }
}