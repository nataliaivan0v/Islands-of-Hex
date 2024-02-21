package islands.model.student;

import islands.model.*;

/**
 * A player applying alpha-beta pruning to the minimax algorithm.
 */
public class AlphaBetaPlayer extends MinimaxPlayer {
    private final TranspositionTable table = new TranspositionTable();

    @Override
    public String getName() {
        return "Alpha-Beta";
    }

    @Override
    public RowColPair chooseNextMove(GameModel model, TileColor tileColor) {
        Move move = getMyMove(model, MAX_DEPTH, Double.MIN_VALUE, Double.MAX_VALUE, tileColor);
        return move.getPosition();
    }

    // Enables TournamentPlayer to select depth of search.
    public RowColPair chooseNextMove(GameModel model, int depth, TileColor tileColor) {
        Move move = getMyMove(model, depth, Double.MIN_VALUE, Double.MAX_VALUE, tileColor);
        return move.getPosition();
    }

    // This doesn't override the ordinary getMyMove() method because it adds
    // alpha and beta parameters.
    private Move getMyMove(GameModel model, int depth, double alpha, double beta, TileColor tileColor) {
        if (depth < 0) {
            throw new IllegalArgumentException();
        }
        if (depth == 0 || model.isGameOver()) {
            return new Move(getValue(model, tileColor));
        }
        double value = -Double.MAX_VALUE;
        Move bestMove = null;
        for (RowColPair move : getLegalPositions(model)) {
            GameModel copy = model.deepCopy();
            copy.makePlay(move.row(), move.column(), tileColor);
            double opponentVal = getOpponentValue(copy, depth - 1, alpha, beta, tileColor.getOpposite());
            if (value < opponentVal) {
                bestMove = new Move(move.row(), move.column(), opponentVal);
            }
            value = Math.max(value, opponentVal);
            alpha = Math.max(alpha, value);
            if (beta <= alpha) {
                break;
            }
        }
        return bestMove;
    }

    // This doesn't override the ordinary getMyMove() method because it adds
    // alpha and beta parameters.
    private double getOpponentValue(GameModel model, int depth, double alpha, double beta, TileColor tileColor) {
        if (depth < 0) {
            throw new IllegalArgumentException();
        }
        if (depth == 0 || model.isGameOver()) {
            return new Move(getValue(model, tileColor)).value();
        }
        double value = Double.MAX_VALUE;
        for (RowColPair move : getLegalPositions(model)) {
            GameModel copy = model.deepCopy();
            copy.makePlay(move.row(), move.column(), tileColor);
            double myVal = getMyMove(model, depth - 1, alpha, beta, tileColor.getOpposite()).value();
            value = Math.min(value, myVal);
            beta = Math.min(beta, value);
            if (beta <= alpha) {
                break;
            }
        }
        return value;
    }
}
