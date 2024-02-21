package islands.model.student.studentname; // TODO: Change package.

import islands.model.GameModel;
import islands.model.RowColPair;
import islands.model.TileColor;
import islands.model.TimeLimitedSimulatedPlayer;

/**
 * A simulated player for a timed tournament.
 */
public class TournamentPlayer extends TimeLimitedSimulatedPlayer {

    @Override
    public String getName() {
        return "STUDENTNAME"; // TODO: Change to your leaderboard name.
    }

    @Override
    public void makeMove(GameModel model, TileColor tileColor, Listener listener) {
        // For now, make a move into the first empty position.
        for (int row = 0; row < model.getSize(); row++) {
            for (int col = 0; col < model.getSize(); col++) {
                if (model.canPlay(row, col)) {
                    listener.receiveMove(new RowColPair(row, col));
                }
            }
        }
    }
}
