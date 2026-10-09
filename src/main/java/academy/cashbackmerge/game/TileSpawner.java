package academy.cashbackmerge.game;

import academy.cashbackmerge.game.model.board.Board;
import academy.cashbackmerge.game.model.board.Position;
import academy.cashbackmerge.model.SpawnRule;
import academy.cashbackmerge.util.Messages;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class TileSpawner {
    private final List<SpawnRule> spawnRules;
    private final Random random;
    private static final int PROBABILITY_SCALE_BASE = 10;

    public TileSpawner(List<SpawnRule> spawnRules, long seed) {
        if (spawnRules == null) {
            throw new IllegalArgumentException(Messages.getString("spawn_rules.error.empty"));
        }
        this.spawnRules = spawnRules;
        this.random = new Random(seed);
    }

    public Board spawnStartTiles(Board board) {
        for (int i = 0; i < 2; i++) {
            board = spawnTile(board);
        }
        return board;
    }

    public Board spawnTile(Board board) {
        List<Position> freeTiles = board.getFreeCells();
        if (freeTiles.isEmpty()) {
            throw new IllegalArgumentException(Messages.getString("board.error.full_board"));
        }
        Position position = freeTiles.get(random.nextInt(freeTiles.size()));
        int randomInt = random.nextInt(Math.powExact(PROBABILITY_SCALE_BASE, SpawnRule.MAX_SCALE));
        BigDecimal target = BigDecimal.valueOf(randomInt).movePointLeft(SpawnRule.MAX_SCALE);
        BigDecimal probabilitySum = BigDecimal.ZERO;
        for (SpawnRule rule : spawnRules) {
            BigDecimal probability = rule.probability();
            probabilitySum = probabilitySum.add(probability);
            if (target.compareTo(probabilitySum) < 0) {
                return board.withTile(position, rule.cashback());
            }
        }
        throw new IllegalArgumentException(Messages.getString("tile_spawner.error.spawn_rule_not_found"));
    }
}
