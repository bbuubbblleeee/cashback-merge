package academy.cashbackmerge.game;

import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.model.board.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class MoveService {

    private MoveService() {}

    public static MoveResult move(Board board, Move move) {
        return switch (move) {
            case LEFT -> leftMove(board.copy(), b -> b);
            case RIGHT -> leftMove(board.reverseRows(), Board::reverseRows);
            case UP -> leftMove(board.transpose(), Board::transpose);
            case DOWN ->
                leftMove(board.transpose().reverseRows(), b -> b.reverseRows().transpose());
        };
    }

    private static MoveResult leftMove(Board board, Function<Board, Board> restoreOrientation) {
        boolean boardChanged = false;
        int score = 0;

        for (int row = 0; row < board.getSize(); row++) {
            MergedRow merged = mergeRow(readRow(board, row));
            boardChanged |= writeRow(board, row, merged.tiles());
            score += merged.score();
        }

        return new MoveResult(boardChanged, restoreOrientation.apply(board), score);
    }

    private static List<Cashback> readRow(Board board, int row) {
        List<Cashback> tiles = new ArrayList<>();
        for (int col = 0; col < board.getSize(); col++) {
            Cashback tile = board.getTileCashback(new Position(row, col));
            if (tile != null) {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    private static boolean writeRow(Board board, int row, List<Cashback> tiles) {
        boolean rowChanged = false;
        for (int col = 0; col < board.getSize(); col++) {
            Position position = new Position(row, col);
            Cashback oldTile = board.getTileCashback(position);
            Cashback newTile = col < tiles.size() ? tiles.get(col) : null;

            if (newTile == null) {
                if (oldTile != null) {
                    board.removeTile(position);
                    rowChanged = true;
                }
            } else if (!newTile.equals(oldTile)) {
                board.setTileCashback(position, newTile);
                rowChanged = true;
            }
        }
        return rowChanged;
    }

    private static MergedRow mergeRow(List<Cashback> tiles) {
        List<Cashback> merged = new ArrayList<>();
        int score = 0;
        int i = 0;

        while (i < tiles.size()) {
            Cashback current = tiles.get(i);
            if (i + 1 < tiles.size() && current.equals(tiles.get(i + 1))) {
                Cashback mergedTile = new Cashback(current.cashback() * 2);
                merged.add(mergedTile);
                score += mergedTile.cashback();
                i += 2;
            } else {
                merged.add(current);
                i++;
            }
        }
        return new MergedRow(merged, score);
    }

    public static boolean hasAvailableMoves(Board board) {
        for (Move move : Move.values()) {
            if (move(board, move).boardChanged()) {
                return true;
            }
        }
        return false;
    }

    private record MergedRow(List<Cashback> tiles, int score) {}
}
