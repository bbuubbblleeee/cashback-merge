package academy.cashbackmerge.game;

import academy.cashbackmerge.exception.InvalidMoveException;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.SessionStatus;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.util.Messages;
import lombok.Getter;

public class GameSession {
    private Board board;
    private final TileSpawner tileSpawner;

    @Getter
    private int movesLeft;

    @Getter
    private int score;

    @Getter
    private SessionStatus sessionStatus;

    public GameSession(Board board, TileSpawner tileSpawner, int movesLeft) {
        if (movesLeft <= 0) {
            throw new IllegalArgumentException(Messages.getString("moves_limit.error.min_value"));
        }
        this.board = board;
        this.tileSpawner = tileSpawner;
        this.movesLeft = movesLeft;
        sessionStatus = SessionStatus.IN_PROGRESS;
    }

    private void completeMove(int moveScore) {
        movesLeft--;
        if (movesLeft <= 0 || !MoveService.hasAvailableMoves(board)) {
            sessionStatus = SessionStatus.FINISHED;
        }
        score += moveScore;
    }

    public MoveResult applyMove(Move move) {
        if (sessionStatus == SessionStatus.FINISHED) {
            throw new IllegalStateException(Messages.getString("session.error.game_is_over"));
        }
        MoveResult result = MoveService.move(board, move);
        if (!result.boardChanged()) {
            throw new InvalidMoveException(Messages.getString("move.error.no_action"));
        }
        this.board = result.board();
        tileSpawner.spawnTile(board);
        completeMove(result.score());
        return result;
    }

    public Board getBoard() {
        return board.copy();
    }
}
