package academy.cashbackmerge.game.model;

import academy.cashbackmerge.model.SessionState;
import academy.cashbackmerge.model.board.Board;

public record GameSession(Board board, SessionState sessionState) {}
