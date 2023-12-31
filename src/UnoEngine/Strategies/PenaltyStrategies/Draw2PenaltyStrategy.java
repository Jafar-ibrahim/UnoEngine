package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public class Draw2PenaltyStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        Player targetPlayer = game.getPlayers().get(game.getCurrentPlayerPosition());
        targetPlayer.drawCards(game.giveCards(2,game.getDrawPile()));
        System.out.println("[Action]    "+game.getCurrentPlayer().getName() +" drew 2 cards");
    }
}
