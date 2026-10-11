package academy.cashbackmerge.model;

import academy.cashbackmerge.model.board.Board;

public record GameSession(Board board, SessionState sessionState) {}
