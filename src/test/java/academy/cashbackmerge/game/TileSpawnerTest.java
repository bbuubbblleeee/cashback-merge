package academy.cashbackmerge.game;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.SpawnRule;
import academy.cashbackmerge.model.board.Board;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Генератор плиток")
public class TileSpawnerTest {
    @Test
    @DisplayName("Генератор корректно генерирует плитку кэшбека")
    void validateSpawnerGenerateOneTile() {
        Board board = new Board(4);
        List<SpawnRule> spawnRules = List.of(new SpawnRule(new Cashback(1), new BigDecimal("1")));
        TileSpawner tileSpawner = new TileSpawner(spawnRules, 1);
        tileSpawner.spawnTile(board);
        assertThat(board.getFreeCells().size()).isEqualTo(board.getSize() * board.getSize() - 1);
    }

    @Test
    @DisplayName("Генератор корректно генерирует стартовые плитки")
    void validateSpawnerGenerateStartTiles() {
        Board board = new Board(4);
        List<SpawnRule> spawnRules = List.of(new SpawnRule(new Cashback(1), new BigDecimal("1")));
        TileSpawner tileSpawner = new TileSpawner(spawnRules, 1);
        tileSpawner.spawnStartTiles(board);
        assertThat(board.getFreeCells().size()).isEqualTo(board.getSize() * board.getSize() - 2);
    }
}
