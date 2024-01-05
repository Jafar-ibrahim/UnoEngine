package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;
import UnoEngine.Player;

public interface PenaltyStrategy {
    void applyPenalty(GameContext gameContext, Player targetPlayer);
}
