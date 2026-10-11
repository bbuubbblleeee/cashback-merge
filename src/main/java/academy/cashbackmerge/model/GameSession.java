package academy.cashbackmerge.model;

import academy.cashbackmerge.model.board.Board;
import lombok.Getter;

@Getter
public class GameSession {
    private Board board;
    private int movesLeft;
    private int currentScore;
    private int currentMaxCashback;
    private GameStatus gameStatus;

    public GameSession(Board board, int movesLeft) {
        this.board = board;
        this.movesLeft = movesLeft;
        currentScore = 0;
        currentMaxCashback = 0;
        gameStatus = GameStatus.IN_PROGRESS;
    }
}
