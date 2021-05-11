package com.jpal.cafci.client;

import com.jpal.cafci.shared.Tuple;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

class YieldReporterTest {

    @Test
    void reports() {
        //given
        var fundClass = FundClass.builder()
                .id("id")
                .name("funds class name")
                .build();
        var _yield = Yield.builder()
                .direct(1)
                .tna(2)
                .accumulated(3)
                .value(4)
                .to(LocalDate.of(2021, Month.JANUARY, 20))
                .from(LocalDate.of(2021, Month.JANUARY, 19))
                .build();
        var input = Stream.of(Tuple.tuple(fundClass, _yield));

        //when
        var result = YieldReporter.reportYields(input);

        //then
        Assertions.assertThat(result.toList()).isEqualTo(List.of(
                "Report of yields",
                "----------------",
                "fund: funds class name day: Wed - 20/1/21 direct: 1,000% accumulated: 3,000%"
        ));
    }

    @Test
    void empty() {
        //given
        var input = Stream.<Tuple>of();
        //when
        var result = YieldReporter.reportYields(input);
        //then
        Assertions.assertThat(result.toList()).isEqualTo(List.of("Report of yields", "----------------"));
    }

}