package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public class SkipPenaltyStrategy implements PenaltyStrategy {
    @Override
    public void applyPenalty(Game game, Player targetPlayer) {
        System.out.println("[Action]    " + targetPlayer.getName() + " turn is skipped ");
        game.advanceTurn();
    }
}
