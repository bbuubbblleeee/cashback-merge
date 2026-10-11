package academy.cashbackmerge.model.board;

import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.util.Messages;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

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

    public Board(Cashback[][] board) {
        if (board.length == 0) {
            throw new IllegalArgumentException(Messages.getString("board_size.error.min_value"));
        }
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null || board[i].length != board.length) {
                throw new IllegalArgumentException(Messages.getString("board_size.error.square"));
            }
        }
        this.size = board.length;
        this.board = board;
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

    public void setTileCashback(Position position, Cashback cashback) {
        checkOutOfBounds(position);
        this.board[position.row()][position.col()] = cashback;
    }

    public void removeTile(Position position) {
        checkOutOfBounds(position);
        this.board[position.row()][position.col()] = null;
    }

    public @Nullable Cashback getTileCashback(Position position) {
        checkOutOfBounds(position);
        int row = position.row();
        int col = position.col();
        return board[row][col] == null ? null : board[row][col];
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

    public Board reverseRows() {
        Board result = new Board(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result.board[i][size - 1 - j] = board[i][j];
            }
        }

        return result;
    }

    public Board transpose() {
        Board result = new Board(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result.board[j][i] = board[i][j];
            }
        }

        return result;
    }

    public Board copy() {
        Cashback[][] copyCashback = new Cashback[size][size];
        for (int i = 0; i < size; i++) {
            if (this.board[i] != null) {
                copyCashback[i] = Arrays.copyOf(this.board[i], size);
            }
        }
        return new Board(copyCashback);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Board boardObj) {
            return Arrays.deepEquals(this.board, boardObj.board);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
