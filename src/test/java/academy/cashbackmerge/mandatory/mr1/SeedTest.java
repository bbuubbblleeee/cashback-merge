package academy.cashbackmerge.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.game.GameHandler;
import academy.cashbackmerge.model.GameSession;
import academy.cashbackmerge.support.TestConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Обязательные тесты: воспроизводимость по seed. */
@DisplayName("MR1. Воспроизводимость по seed")
class SeedTest {

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 42L, 999L})
    @DisplayName("Одинаковый конфиг и seed дают одинаковое начальное поле")
    void sameConfigAndSeedProduceSameInitialBoard(long seed) {
        CampaignConfig config = TestConfig.getConfig();
        GameHandler gameHandler = new GameHandler(config);
        GameSession session1 = gameHandler.startGame(seed);
        GameSession session2 = gameHandler.startGame(seed);

        assertThat(session1.board().equals(session2.board())).isTrue();
    }

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("Одинаковый конфиг, seed и стратегия дают одинаковый отчёт симуляции")
    void sameInputsProduceSameSimulationReport() {
        fail("Тест не реализован");
    }
}
