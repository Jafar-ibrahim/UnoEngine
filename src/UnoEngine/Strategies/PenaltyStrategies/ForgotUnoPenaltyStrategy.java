package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.GameVariations.Game;

public class ForgotUnoPenaltyStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        game.getCurrentPlayer().drawCards(game.giveCards(2,game.getDrawPile()));
    }
}
