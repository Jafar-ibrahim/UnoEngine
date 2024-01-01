package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public class Draw2PenaltyStrategy implements PenaltyStrategy {
    @Override
    public void applyPenalty(Game game, Player targetPlayer) {

        targetPlayer.drawCards(game.giveCards(2, game.getDrawPile()));
        System.out.println("[Action]    " + targetPlayer.getName() + " drew 2 cards");
        new SkipPenaltyStrategy().applyPenalty(game,targetPlayer);
    }
}
