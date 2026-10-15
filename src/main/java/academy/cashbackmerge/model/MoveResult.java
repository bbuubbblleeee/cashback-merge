package academy.cashbackmerge.model;

import academy.cashbackmerge.model.board.Board;

public record MoveResult(boolean boardChanged, Board board, int score) {}
