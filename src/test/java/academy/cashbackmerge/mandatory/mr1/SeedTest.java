package academy.cashbackmerge.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import academy.cashbackmerge.config.model.CampaignConfig;
import academy.cashbackmerge.game.TileSpawner;
import academy.cashbackmerge.game.model.GameSession;
import academy.cashbackmerge.game.model.board.Board;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Reward;
import academy.cashbackmerge.model.SpawnRule;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: воспроизводимость по seed. */
@DisplayName("MR1. Воспроизводимость по seed")
class SeedTest {

    @Test
    @DisplayName("Одинаковый конфиг и seed дают одинаковое начальное поле")
    void sameConfigAndSeedProduceSameInitialBoard() {
        CampaignConfig config = validConfig();
        Board first = new TileSpawner(config.spawnRules(), 1).spawnStartTiles(new Board(4));
        Board second = new TileSpawner(config.spawnRules(), 1).spawnStartTiles(new Board(4));
        GameSession session1 = new GameSession(first, config.movesLimit());
        GameSession session2 = new GameSession(second, config.movesLimit());
        assertThat(session1.getBoard().equals(session2.getBoard())).isTrue();
    }

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("Одинаковый конфиг, seed и стратегия дают одинаковый отчёт симуляции")
    void sameInputsProduceSameSimulationReport() {
        fail("Тест не реализован");
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
