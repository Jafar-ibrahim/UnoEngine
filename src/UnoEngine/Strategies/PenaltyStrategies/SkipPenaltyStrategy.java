package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;
import UnoEngine.Player;

public class SkipPenaltyStrategy implements PenaltyStrategy {
    @Override
    public void applyPenalty(GameContext gameContext, Player targetPlayer) {
        System.out.println("[Action]    " + targetPlayer.getName() + " turn is skipped ");
        gameContext.getTurnManager().advanceTurn();
    }
}
