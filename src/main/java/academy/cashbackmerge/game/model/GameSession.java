package academy.cashbackmerge.game.model;

import academy.cashbackmerge.game.model.board.Board;

public record GameSession(Board board, SessionState sessionState) {}
