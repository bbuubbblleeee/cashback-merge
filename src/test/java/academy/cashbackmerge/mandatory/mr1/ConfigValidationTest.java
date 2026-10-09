package academy.cashbackmerge.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.cashbackmerge.config.model.CampaignConfig;
import academy.cashbackmerge.config.validator.ConfigValidator;
import academy.cashbackmerge.exception.IllegalConfigException;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.SpawnRule;
import academy.cashbackmerge.support.TestConfig;
import academy.cashbackmerge.util.Messages;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Обязательные тесты: конфиг кампании. */
@DisplayName("MR1. Конфиг кампании")
class ConfigValidationTest {
    @Test
    @DisplayName("Валидный конфиг принимается")
    void validConfigIsAccepted() {
        CampaignConfig config = TestConfig.getConfig();
        assertThatCode(() -> ConfigValidator.validate(config)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({"0.5, 0.4", "0.6, 0.5"})
    @DisplayName("Сумма вероятностей spawn-правил не равна 1 — конфиг отклоняется")
    void probabilitiesNotSummingToOneAreRejected(String probability1, String probability2) {
        List<SpawnRule> spawnRules = List.of(
                new SpawnRule(new Cashback(1), new BigDecimal(probability1)),
                new SpawnRule(new Cashback(2), new BigDecimal(probability2)));
        CampaignConfig config =
                TestConfig.getConfig().toBuilder().spawnRules(spawnRules).build();
        assertThatThrownBy(() -> ConfigValidator.validate(config))
                .isInstanceOf(IllegalConfigException.class)
                .hasMessage(Messages.getString("probability.error.sum"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("movesLimit <= 0 — конфиг отклоняется")
    void nonPositiveMovesLimitIsRejected(int movesLimit) {
        CampaignConfig config =
                TestConfig.getConfig().toBuilder().movesLimit(movesLimit).build();
        assertThatThrownBy(() -> ConfigValidator.validate(config))
                .isInstanceOf(IllegalConfigException.class)
                .hasMessage(Messages.getString("moves_limit.error.min_value"));
    }

    @Test
    @DisplayName("Пустой список spawn-правил — конфиг отклоняется")
    void emptySpawnRulesAreRejected() {
        CampaignConfig config =
                TestConfig.getConfig().toBuilder().spawnRules(List.of()).build();
        assertThatThrownBy(() -> ConfigValidator.validate(config))
                .isInstanceOf(IllegalConfigException.class)
                .hasMessage(Messages.getString("spawn_rules.error.empty"));
    }
}
