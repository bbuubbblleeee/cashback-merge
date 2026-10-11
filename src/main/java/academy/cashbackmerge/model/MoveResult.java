package academy.cashbackmerge.model;

import academy.cashbackmerge.model.board.Board;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MoveResult {
    private final boolean boardChanged;
    private final Board board;
}
