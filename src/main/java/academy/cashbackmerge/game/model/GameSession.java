package academy.cashbackmerge.game.model;

import academy.cashbackmerge.game.model.board.Board;
import lombok.Getter;

@Getter
public class GameSession {
    private final Board board;
    private final SessionState sessionState;

    public GameSession(Board board, int movesLimit) {
        this.board = board;
        this.sessionState = new SessionState(movesLimit);
    }
}
