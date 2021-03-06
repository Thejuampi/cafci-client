package com.jpal.cafci.client;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;

class FundRepositoryShould {

    @Test
    void save_funds() {
        // given
        FundRepository repo = FundRepository.withInitialValue(emptyMap());
        val fund = randomFund();

        //when
        repo.set(Map.of("fund-id", fund));

        //then
        assertThat(repo.cache.get()).containsValue(fund);
    }

    @Test
    void returns_previous_value_on_save_all() {
        val initialValue = Map.of("id", randomFund());
        val repo = FundRepository.withInitialValue(initialValue);

        val result = repo.set(Map.of());

        assertThat(result).isEqualTo(initialValue);
    }

    @Test
    void returns_fund_classes() {
        val initialValue = Map.of("id", randomFund());
        val repo = FundRepository.withInitialValue(initialValue);

        val result = repo.findByClassNameRegex("fund-class-name");
        
        assertThat(result).isNotEmpty();
    }

    private Fund randomFund() {
        return new Fund("fund-id",
                "fund-name",
                "fund-objective",
                List.of(randomFundClass()));
    }

    private FundClass randomFundClass() {
        return new FundClass(
                "fund-class-id",
                "fund-class-name");
    }
}