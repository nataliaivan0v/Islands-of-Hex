package islands.model.student;

import islands.model.GameModel;
import islands.model.TileColor;

/**
 * A player that chooses the highest-scoring move based on the assumption
 * that the opponent will always choose randomly.
 */
public class RandomMaxPlayer extends MinimaxPlayer {
    @Override
    public String getName() {
        return "RandomMax";
    }

    @Override
    public double getOpponentValue(GameModel model, int depth, TileColor tileColor) {
        if (depth < 0) {
            throw new IllegalArgumentException();
        }
        if (depth == 0 || model.isGameOver()) {
            return getValue(model, tileColor);
        }
        double sumValues = 0;
        for (islands.model.RowColPair position : getLegalPositions(model)) {
            GameModel childModel = model.deepCopy();
            childModel.makePlay(position.row(), position.column(), tileColor);
            islands.model.Move childMove = getMyMove(childModel, depth - 1, tileColor.getOpposite());
            sumValues += childMove.value();
        }
        return sumValues / getLegalPositions(model).size();
    }
}
