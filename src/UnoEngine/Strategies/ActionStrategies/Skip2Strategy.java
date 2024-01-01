package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;

public class Skip2Strategy implements ActionStrategy{
    @Override
    public void applyAction(Game game) {
        game.getNextPlayer(1).setPenalty(StandardPenalty.SKIP);
        // In case of 2 players only , only one skip is performed
        if(game.getNoOfPlayers() > 2 )
            game.getNextPlayer(2).setPenalty(StandardPenalty.SKIP);
    }
}
