package islands.model.student;

import islands.model.GameModel;
import islands.model.Move;
import islands.model.TileColor;

/**
 * A player that uses caching to improve on the minimax algorithm.
 *
 * @see TranspositionTable
 */
public class CachingMinimaxPlayer extends MinimaxPlayer {
    private TranspositionTable table = new TranspositionTable();

    @Override
    public String getName() {
        return "Caching Minimax";
    }

    @Override
    public Move getMyMove(GameModel model, int depth, TileColor tileColor) {
        if (table.hasMove(model, depth)) {
            return table.getMove(model, depth);
        }
        // For now, always call the superclass (Minimax) method.
        Move move = super.getMyMove(model, depth, tileColor);
        table.putMove(model, depth, move);
        return move;
    }
}