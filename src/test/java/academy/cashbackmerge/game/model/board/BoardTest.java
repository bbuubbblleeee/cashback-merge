package academy.cashbackmerge.game.model.board;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.model.board.Position;
import academy.cashbackmerge.util.Messages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Поле с плитками")
public class BoardTest {
    @ParameterizedTest
    @CsvSource({"-1, 0", "0, -1", "4, 0", "0, 4"})
    @DisplayName("Координаты находятся в границах")
    void validateCoordinatesInBounds(int a, int b) {
        Board board = new Board(4);
        assertThatThrownBy(() -> board.withTile(new Position(a, b), new Cashback(2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Messages.getString("board.error.out_of_bounds"));
    }
}
