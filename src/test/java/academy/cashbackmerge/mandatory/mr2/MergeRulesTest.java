package academy.cashbackmerge.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import academy.cashbackmerge.game.MoveService;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.model.board.Position;
import java.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        Cashback[] beforeCashbacks = parseCashback(before);
        Cashback[] expectedCashbacks = parseCashback(expected);

        Board board = new Board(beforeCashbacks.length);
        for (int i = 0; i < beforeCashbacks.length; i++) {
            board.setTileCashback(new Position(0, i), beforeCashbacks[i]);
        }

        MoveResult result = MoveService.MOVES.get(Move.LEFT).apply(board);
        Board newBoard = result.getBoard();

        Cashback[] resultCashbacks = getResultCashbacksFromBoard(newBoard);
        assertThat(Arrays.equals(expectedCashbacks, resultCashbacks)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "'1,1,.,.', '.,.,.,2'",
        "'1,1,1,.', '.,.,1,2'",
        "'1,1,1,1', '.,.,2,2'",
        "'2,2,4,4', '.,.,4,8'",
    })
    @DisplayName("Сдвиг вправо")
    void rowShiftsRightAndMerges(String before, String expected) {
        Cashback[] beforeCashbacks = parseCashback(before);
        Cashback[] expectedCashbacks = parseCashback(expected);

        Board board = new Board(beforeCashbacks.length);
        for (int i = 0; i < beforeCashbacks.length; i++) {
            board.setTileCashback(new Position(0, i), beforeCashbacks[i]);
        }

        MoveResult result = MoveService.MOVES.get(Move.RIGHT).apply(board);
        Board newBoard = result.getBoard();

        Cashback[] resultCashbacks = getResultCashbacksFromBoard(newBoard);
        assertThat(Arrays.equals(expectedCashbacks, resultCashbacks)).isTrue();
    }

    private Cashback[] parseCashback(String cashbacksString) {
        String[] values = cashbacksString.split(",");
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

    private Cashback[] getResultCashbacksFromBoard(Board board) {
        Cashback[] resultCashbacks = new Cashback[board.getSize()];
        for (int i = 0; i < board.getSize(); i++) {
            resultCashbacks[i] = board.getTileCashback(new Position(0, i));
        }
        return resultCashbacks;
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Одна плитка не сливается дважды за ход: [1,1,2,.] -> [2,2,.,.]")
    void tileDoesNotMergeTwiceInOneMove() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Слияние работает во всех четырёх направлениях")
    void mergeWorksInAllDirections() {
        fail("Тест не реализован");
    }
}
