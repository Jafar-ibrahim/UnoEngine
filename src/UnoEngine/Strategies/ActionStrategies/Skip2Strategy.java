package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;
import UnoEngine.GameVariations.TurnManager;

public class Skip2Strategy implements ActionStrategy{
    @Override
    public void applyAction(GameContext gameContext) {
        TurnManager turnManager = gameContext.getTurnManager();
        turnManager.getNextPlayer(1).setPenalty(StandardPenalty.SKIP);
        // In case of 2 players only , only one skip is performed
        if(gameContext.getPlayersManager().getNoOfPlayers() > 2 )
            turnManager.getNextPlayer(2).setPenalty(StandardPenalty.SKIP);
    }

}
