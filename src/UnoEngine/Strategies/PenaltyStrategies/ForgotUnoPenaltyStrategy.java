package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.GameVariations.Game;

public class ForgotUnoPenaltyStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        System.out.println("[Penalty]    Player " + game.getCurrentPlayer().getName() + " didn't call Uno in time.");
        game.getCurrentPlayer().drawCards(game.giveCards(2,game.getDrawPile()));
    }
}
