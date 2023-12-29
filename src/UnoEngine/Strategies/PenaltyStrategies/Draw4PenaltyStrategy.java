package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public class Draw4PenaltyStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        Player targetPlayer = game.getPlayers().get(game.getCurrentPlayerPosition());
        targetPlayer.drawCards(game.giveCards(4,game.getDrawPile()));
        System.out.println("[Action]    "+game.getCurrentPlayer().getName() +" drew 4 cards");
        game.processPenalty(Penalty.SKIP);
    }
}
