package academy.cashbackmerge.game;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.model.GameSession;
import academy.cashbackmerge.model.SessionState;
import academy.cashbackmerge.model.board.Board;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameHandler {
    private final CampaignConfig config;

    public GameSession startGame(long seed) {
        Board board = new Board(config.boardSize());
        TileSpawner tileSpawner = new TileSpawner(config.spawnRules(), seed);
        board = tileSpawner.spawnStartTiles(board);
        return new GameSession(board, new SessionState(config.movesLimit()));
    }
}
