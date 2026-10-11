package academy.cashbackmerge.game;

import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.model.board.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class MoveService {
    public static final Map<Move, Function<Board, MoveResult>> MOVES;

    private MoveService() {}

    static {
        MOVES = Map.of(
                Move.LEFT,
                MoveService::leftMove,
                Move.RIGHT,
                MoveService::rightMove,
                Move.UP,
                MoveService::upMove,
                Move.DOWN,
                MoveService::downMove);
    }

    private static MoveResult leftMove(Board board) {
        boolean boardChanged = false;
        int size = board.getSize();

        for (int i = 0; i < size; i++) {
            List<Cashback> tiles = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                Cashback tile = board.getTileCashback(new Position(i, j));
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            List<Cashback> merged = mergeCashback(tiles);

            for (int j = 0; j < size; j++) {
                Position position = new Position(i, j);
                boardChanged |= applyBoardChanges(merged, j, position, board);
            }
        }

        return new MoveResult(boardChanged, board);
    }

    private static MoveResult rightMove(Board board) {
        board = board.reverseRows();
        MoveResult moveResult = leftMove(board);
        return new MoveResult(moveResult.isBoardChanged(), board.reverseRows());
    }

    private static MoveResult upMove(Board board) {
        board = board.transpose();
        MoveResult moveResult = leftMove(board);
        return new MoveResult(moveResult.isBoardChanged(), board.transpose());
    }

    private static MoveResult downMove(Board board) {
        board = board.transpose().reverseRows();
        MoveResult moveResult = leftMove(board);
        return new MoveResult(moveResult.isBoardChanged(), board.reverseRows().transpose());
    }

    private static boolean applyBoardChanges(List<Cashback> merged, int j, Position position, Board board) {
        boolean boardChanged = false;
        Cashback newTile = j < merged.size() ? merged.get(j) : null;
        Cashback oldTile = board.getTileCashback(position);

        if (newTile == null) {
            if (oldTile != null) {
                boardChanged = true;
                board.removeTile(position);
            }
        } else {
            if (!newTile.equals(oldTile)) {
                boardChanged = true;
                board.setTileCashback(position, newTile);
            }
        }
        return boardChanged;
    }

    private static List<Cashback> mergeCashback(List<Cashback> tiles) {
        List<Cashback> merged = new ArrayList<>();
        int j = 0;

        while (j < tiles.size()) {
            Cashback current = tiles.get(j);

            if (j + 1 < tiles.size() && current.equals(tiles.get(j + 1))) {
                merged.add(new Cashback(current.cashback() * 2));
                j += 2;
            } else {
                merged.add(current);
                j++;
            }
        }
        return merged;
    }
}
