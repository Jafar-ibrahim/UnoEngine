package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.GameVariations.Game;

public class SkipPenaltyStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        System.out.println(game.getCurrentPlayer().getName()+" turn is skipped ");
        game.advanceTurn();
    }
}
