package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public class Draw4PenaltyStrategy implements PenaltyStrategy {
    @Override
    public void applyPenalty(Game game, Player targetPlayer) {

        targetPlayer.drawCards(game.giveCards(4, game.getDrawPile()));
        System.out.println("[Action]    " + targetPlayer.getName() + " drew 4 cards");
        new SkipPenaltyStrategy().applyPenalty(game,targetPlayer);
    }
}
