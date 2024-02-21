package islands.model.student;

import islands.model.GameModel;
import islands.model.Move;
import islands.model.TileColor;

import java.util.*;

/**
 * A table for storing and retrieving the results of calls to {@link
 * islands.model.SimulatedGameTreePlayer#getMyMove(GameModel, int, TileColor)}
 */
public class TranspositionTable {
    private record Value(int depth, Move move) {
    }

    private final Map<String, Value> hash = new HashMap<>();

    /**
     * Records that calling {@link
     * islands.model.SimulatedGameTreePlayer#getMyMove(GameModel, int, TileColor)}
     * with the given model and depth produced the specified move.
     *
     * @param model the model
     * @param depth the depth
     * @param move  the move
     */
    public void putMove(GameModel model, int depth, Move move) {
        hash.put(model.toString(), new Value(depth, move));
        putTransformations(model.getSize(), model.toString(), new Value(depth, move));
    }

    // Adds entries for each board representation equivalent (through
    // rotation or reflection) with the passed board string.
    private void putTransformations(int size, String boardString, Value value) {
        String[] rows = boardString.split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            sb.append(new StringBuilder(rows[i]).reverse());
            sb.append("\n");
        }
        String reversed = sb.toString();

        int x = size - 1 - value.move.row();
        int y = size - 1 - value.move.col();
        Move move = new Move(x, y, value.move.value());
        hash.put(reversed, new Value(value.depth, move));
    }

    /**
     * Checks whether this table has the move recommended for this model
     * (or one of its transformations) when searching to the specified depth or
     * deeper.
     *
     * @param model the model
     * @param depth the minimum search depth
     * @return true if a move is available, false otherwise
     */
    public boolean hasMove(GameModel model, int depth) {
        String key = model.toString();
        if (!hash.containsKey(key)) {
            return false;
        }
        Value value = hash.get(key);
        return value.depth >= depth;
    }

    /**
     * Gets the stored move for this model computed to the given depth or
     * deeper.
     *
     * @param model the model
     * @param depth the depth
     * @return the stored move
     * @throws NoSuchElementException if this table does not have an entry
     *                                with the specified model with a depth
     *                                greater than or equal to the
     *                                requested depth
     */
    public Move getMove(GameModel model, int depth) {
        String key = model.toString();
        if (!hash.containsKey(key)) {
            throw new NoSuchElementException();
        }
        int currDepth = hash.get(key).depth;
        if (depth > currDepth) {
            throw new NoSuchElementException();
        }
        return hash.get(key).move();
    }
}
