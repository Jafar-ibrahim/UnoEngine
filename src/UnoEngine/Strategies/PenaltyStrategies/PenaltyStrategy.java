package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public interface PenaltyStrategy {
    void applyPenalty(Game game, Player targetPlayer);
}
