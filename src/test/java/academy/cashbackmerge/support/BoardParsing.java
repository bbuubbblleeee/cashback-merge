package academy.cashbackmerge.support;

import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.util.Messages;

public final class BoardParsing {
    private BoardParsing() {}

    public static Cashback[] parseCashback(String cashbacksString, int size) {
        String[] values = cashbacksString.trim().split(",");
        if (values.length != size) {
            throw new IllegalArgumentException(Messages.getString("board_size.error.square"));
        }
        Cashback[] cashbacks = new Cashback[values.length];
        for (int i = 0; i < values.length; i++) {
            Cashback cashback = null;
            if (!values[i].equals(".")) {
                cashback = new Cashback(Integer.parseInt(values[i]));
            }
            cashbacks[i] = cashback;
        }
        return cashbacks;
    }

    public static Board parseBoard(String boardString) {
        String[] rows = boardString.trim().split("\n");
        Cashback[][] cashbacks = new Cashback[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            cashbacks[i] = parseCashback(rows[i], rows.length);
        }
        return new Board(cashbacks);
    }
}
