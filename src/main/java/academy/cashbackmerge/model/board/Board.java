package academy.cashbackmerge.model.board;

import academy.cashbackmerge.game.model.board.Position;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.util.Messages;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

public final class Board {
    @Getter
    private final int size;

    private final Cashback[][] board;

    public Board(int boardSize) {
        if (boardSize <= 0) {
            throw new IllegalArgumentException(Messages.getString("board_size.error.min_value"));
        }
        this.size = boardSize;
        this.board = new Cashback[boardSize][boardSize];
    }

    public String getBoardString() {
        StringBuilder boardBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (j != 0) {
                    boardBuilder.append(" | ");
                }
                String cellValue;
                if (board[i][j] == null) {
                    cellValue = ".";
                } else {
                    cellValue = board[i][j].toString();
                }
                boardBuilder.append(String.format("%-4s", cellValue));
            }
            boardBuilder.append(System.lineSeparator());
        }
        return boardBuilder.toString();
    }

    public Board withTile(Position position, Cashback cashback) {
        Objects.requireNonNull(cashback);
        checkOutOfBounds(position);
        Board newBoard = new Board(size);
        copyCells(newBoard);
        newBoard.board[position.row()][position.col()] = cashback;
        return newBoard;
    }

    public Board withoutTile(Position position) {
        checkOutOfBounds(position);
        Board newBoard = new Board(size);
        copyCells(newBoard);
        newBoard.board[position.row()][position.col()] = null;
        return newBoard;
    }

    private void checkOutOfBounds(Position position) {
        if (position.row() < 0 || position.row() >= size || position.col() < 0 || position.col() >= size) {
            throw new IllegalArgumentException(Messages.getString("board.error.out_of_bounds"));
        }
    }

    public List<Position> getFreeCells() {
        List<Position> freeCells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    freeCells.add(new Position(i, j));
                }
            }
        }
        return freeCells;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Board boardObj) {
            return Arrays.deepEquals(this.board, boardObj.board);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    private void copyCells(Board newBoard) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newBoard.board[i][j] = this.board[i][j];
            }
        }
    }
}
