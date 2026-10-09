package academy.cashbackmerge.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.cashbackmerge.config.exception.IllegalConfigException;
import academy.cashbackmerge.config.model.CampaignConfig;
import academy.cashbackmerge.config.validator.ConfigValidator;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Reward;
import academy.cashbackmerge.model.SpawnRule;
import academy.cashbackmerge.util.Messages;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Обязательные тесты: конфиг кампании. */
@DisplayName("MR1. Конфиг кампании")
class ConfigValidationTest {
    @Test
    @DisplayName("Валидный конфиг принимается")
    void validConfigIsAccepted() {
        CampaignConfig config = validConfig();
        assertThatCode(() -> ConfigValidator.validate(config)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Сумма вероятностей spawn-правил не равна 1 — конфиг отклоняется")
    void probabilitiesNotSummingToOneAreRejected() {
        List<SpawnRule> spawnRules = List.of(
                new SpawnRule(new Cashback(1), new BigDecimal("0.15")),
                new SpawnRule(new Cashback(2), new BigDecimal("0.15")));
        CampaignConfig config = validConfig().toBuilder().spawnRules(spawnRules).build();
        assertThatThrownBy(() -> ConfigValidator.validate(config))
                .isInstanceOf(IllegalConfigException.class)
                .hasMessage(Messages.getString("probability.error.sum"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("movesLimit <= 0 — конфиг отклоняется")
    void nonPositiveMovesLimitIsRejected(int movesLimit) {
        CampaignConfig config = validConfig().toBuilder().movesLimit(movesLimit).build();
        assertThatThrownBy(() -> ConfigValidator.validate(config))
                .isInstanceOf(IllegalConfigException.class)
                .hasMessage(Messages.getString("moves_limit.error.min_value"));
    }

    @Test
    @DisplayName("Пустой список spawn-правил — конфиг отклоняется")
    void emptySpawnRulesAreRejected() {
        CampaignConfig config = validConfig().toBuilder().spawnRules(List.of()).build();
        assertThatThrownBy(() -> ConfigValidator.validate(config))
                .isInstanceOf(IllegalConfigException.class)
                .hasMessage(Messages.getString("spawn_rules.error.empty"));
    }

    CampaignConfig validConfig() {
        List<SpawnRule> spawnRules = List.of(
                new SpawnRule(new Cashback(1), new BigDecimal("0.15")),
                new SpawnRule(new Cashback(2), new BigDecimal("0.85")));
        List<Reward> rewardTable = List.of(new Reward(new Cashback(1), 0), new Reward(new Cashback(2), 1000));
        return CampaignConfig.builder()
                .boardSize(4)
                .movesLimit(10)
                .spawnRules(spawnRules)
                .rewardTable(rewardTable)
                .maxRewardCashback(new Cashback(32))
                .maxRewardAllowedRate(new BigDecimal("0.20"))
                .build();
    }
}
