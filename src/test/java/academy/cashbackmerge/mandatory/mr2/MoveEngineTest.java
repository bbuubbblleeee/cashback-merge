package academy.cashbackmerge.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import academy.cashbackmerge.exception.InvalidMoveException;
import academy.cashbackmerge.game.TileSpawner;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.GameSession;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.SessionStatus;
import academy.cashbackmerge.model.SpawnRule;
import academy.cashbackmerge.model.board.Board;
import academy.cashbackmerge.model.board.Position;
import academy.cashbackmerge.support.BoardParsing;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Обязательные тесты: движок ходов — что считается ходом и что после него происходит. */
@DisplayName("MR2. Движок ходов")
@ExtendWith(MockitoExtension.class)
class MoveEngineTest {
    @Mock
    private TileSpawner mockSpawner;

    @Test
    @DisplayName("Невалидный ход не меняет поле, не тратит ход и не создаёт плитку")
    void invalidMoveChangesNothing() {
        Board board = BoardParsing.parseBoard("""
            1,2,4,8
            2,4,8,.
            32,64,128,256
            512,2,1,2
            """);
        Board before = board.copy();
        GameSession session = new GameSession(board, mockSpawner, 1);
        assertThatThrownBy(() -> session.applyMove(Move.LEFT)).isInstanceOf(InvalidMoveException.class);
        verify(mockSpawner, never()).spawnTile(any());
        assertThat(session.getBoard()).isEqualTo(before);
        assertThat(session.getMovesLeft()).isEqualTo(1);
    }

    @Test
    @DisplayName("После валидного хода появляется ровно одна новая плитка")
    void validMoveSpawnsExactlyOneTile() {
        TileSpawner tileSpawner = new TileSpawner(List.of(new SpawnRule(new Cashback(1), new BigDecimal("1"))), 1);
        Board board = BoardParsing.parseBoard("""
            1,1,.,.
            .,.,.,.
            .,.,.,.
            .,.,.,.
            """);
        GameSession session = new GameSession(board, tileSpawner, 1);
        session.applyMove(Move.LEFT);
        assertThat(session.getBoard().getFreeCells()).hasSize(14);
    }

    @Test
    @DisplayName("После N валидных ходов игра завершена")
    void gameEndsAfterMovesLimit() {
        TileSpawner tileSpawner = new TileSpawner(List.of(new SpawnRule(new Cashback(1), new BigDecimal("1"))), 1);
        Board board = BoardParsing.parseBoard("""
            1,1,.,.
            2,.,.,.
            .,.,.,.
            .,.,.,.
            """);
        GameSession session = new GameSession(board, tileSpawner, 2);
        session.applyMove(Move.LEFT);
        assertThat(session.getSessionStatus()).isEqualTo(SessionStatus.IN_PROGRESS);
        session.applyMove(Move.UP);
        assertThat(session.getSessionStatus()).isEqualTo(SessionStatus.FINISHED);
    }

    @Test
    @DisplayName("Достижение плитки 32% не заканчивает игру — доигрываем до лимита ходов")
    void reachingMaxTileDoesNotEndGame() {
        TileSpawner tileSpawner = new TileSpawner(List.of(new SpawnRule(new Cashback(1), new BigDecimal("1"))), 1);
        Board board = BoardParsing.parseBoard("""
            16,16,.,.
            1,.,.,.
            1,.,.,.
            .,.,.,.
            """);
        GameSession session = new GameSession(board, tileSpawner, 2);
        session.applyMove(Move.LEFT);
        assertThat(session.getSessionStatus()).isEqualTo(SessionStatus.IN_PROGRESS);
        assertThat(session.getBoard().getTileCashback(new Position(0, 0))).isEqualTo(new Cashback(32));
        session.applyMove(Move.UP);
        assertThat(session.getSessionStatus()).isEqualTo(SessionStatus.FINISHED);
    }

    @Test
    @DisplayName("Score считается по правилам и растёт только при слияниях")
    void scoreIsCalculatedOnMerges() {
        TileSpawner tileSpawner = new TileSpawner(List.of(new SpawnRule(new Cashback(1), new BigDecimal("1"))), 1);
        Board board = BoardParsing.parseBoard("""
            2,.,2,.
            .,.,.,.
            .,.,.,.
            .,.,.,.
            """);
        GameSession session = new GameSession(board, tileSpawner, 2);
        session.applyMove(Move.LEFT);
        assertThat(session.getScore()).isEqualTo(4);
        session.applyMove(Move.DOWN);
        assertThat(session.getScore()).isEqualTo(4);
    }
}
