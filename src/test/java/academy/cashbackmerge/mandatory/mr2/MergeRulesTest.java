package academy.cashbackmerge.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cashbackmerge.game.MoveService;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.support.BoardParsing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Обязательные тесты: правила слияния плиток. */
@DisplayName("MR2. Слияние плиток")
class MergeRulesTest {

    @ParameterizedTest
    @CsvSource({
        "'1,1,.,.', '2,.,.,.'",
        "'1,1,1,.', '2,1,.,.'",
        "'1,1,1,1', '2,2,.,.'",
        "'2,2,4,4', '4,8,.,.'",
    })
    @DisplayName("Сдвиг влево: {0} -> {1}")
    void rowShiftsLeftAndMerges(String before, String expected) {
        Cashback[] beforeCashbacksRow = BoardParsing.parseCashback(before, 4);
        Cashback[] expectedCashbacksRow = BoardParsing.parseCashback(expected, 4);

        Cashback[][] beforeCashbacks = new Cashback[beforeCashbacksRow.length][beforeCashbacksRow.length];
        beforeCashbacks[0] = beforeCashbacksRow;

        Cashback[][] expectedCashbacks = new Cashback[expectedCashbacksRow.length][expectedCashbacksRow.length];
        expectedCashbacks[0] = expectedCashbacksRow;

        Board beforeBoard = new Board(beforeCashbacks);
        Board expectedBoard = new Board(expectedCashbacks);

        MoveResult result = MoveService.move(beforeBoard, Move.LEFT);

        assertThat(result.board()).isEqualTo(expectedBoard);
    }

    @ParameterizedTest
    @CsvSource({"'1,1,2,.', '2,2,.,.'", "'2,2,2,2', '4,4,.,.'"})
    @DisplayName("Одна плитка не сливается дважды за ход: [1,1,2,.] -> [2,2,.,.]")
    void tileDoesNotMergeTwiceInOneMove(String before, String expected) {
        Cashback[] beforeCashbacksRow = BoardParsing.parseCashback(before, 4);
        Cashback[] expectedCashbacksRow = BoardParsing.parseCashback(expected, 4);

        Cashback[][] beforeCashbacks = new Cashback[beforeCashbacksRow.length][beforeCashbacksRow.length];
        beforeCashbacks[0] = beforeCashbacksRow;

        Cashback[][] expectedCashbacks = new Cashback[expectedCashbacksRow.length][expectedCashbacksRow.length];
        expectedCashbacks[0] = expectedCashbacksRow;

        Board beforeBoard = new Board(beforeCashbacks);
        Board expectedBoard = new Board(expectedCashbacks);

        MoveResult result = MoveService.move(beforeBoard, Move.LEFT);

        assertThat(result.board()).isEqualTo(expectedBoard);
    }

    @ParameterizedTest
    @CsvSource({
        "'1,1,.,.\n1,1,1,.\n1,1,1,1\n2,2,4,4', '2,.,.,.\n2,1,.,.\n2,2,.,.\n4,8,.,.', 'LEFT'",
        "'1,1,.,.\n1,1,1,.\n1,1,1,1\n2,2,4,4', '.,.,.,2\n.,.,1,2\n.,.,2,2\n.,.,4,8', 'RIGHT'",
        "'1,1,.,.\n1,1,1,.\n1,1,1,1\n2,2,4,4', '2,2,2,1\n1,1,4,4\n2,2,.,.\n.,.,.,.', 'UP'",
        "'1,1,.,.\n1,1,1,.\n1,1,1,1\n2,2,4,4', '.,.,.,.\n1,1,.,.\n2,2,2,1\n2,2,4,4', 'DOWN'"
    })
    @DisplayName("Слияние работает во всех четырёх направлениях")
    void mergeWorksInAllDirections(String before, String expected, Move move) {
        Board beforeBoard = BoardParsing.parseBoard(before);
        Board expectedBoard = BoardParsing.parseBoard(expected);
        MoveResult result = MoveService.move(beforeBoard, move);
        assertThat(result.board()).isEqualTo(expectedBoard);
    }
}
