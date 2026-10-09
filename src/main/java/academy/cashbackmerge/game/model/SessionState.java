package academy.cashbackmerge.game.model;

import academy.cashbackmerge.util.Messages;

public class SessionState {
    private final int movesLimit;
    private int movesDone = 0;

    public SessionState(int movesLimit) {
        if (movesLimit <= 0) {
            throw new IllegalArgumentException(Messages.getString("moves_limit.error.min_value"));
        }
        this.movesLimit = movesLimit;
    }

    public boolean isFinished() {
        return movesDone >= movesLimit;
    }

    public void incrementMovesDone() {
        movesDone++;
    }
}
