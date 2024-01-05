package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.GameDirection;
import UnoEngine.Enums.NormalAction;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;
import UnoEngine.GameVariations.GameStateManager;

public class ReverseActionStrategy implements ActionStrategy {
    @Override
    public void applyAction(GameContext gameContext) {
        GameStateManager gameStateManager = gameContext.getGameStateManager();
        if(gameStateManager.getGameDirection() == GameDirection.CLOCKWISE)
            gameStateManager.setGameDirection(GameDirection.COUNTER_CLOCKWISE);
        else
            gameStateManager.setGameDirection(GameDirection.CLOCKWISE);

        System.out.println("[Action]    Game direction got reversed");

        // In case the number of players is 2 , reverse action becomes a skip penalty to the other player
        if(gameContext.getPlayersManager().getNoOfPlayers() == 2 ){
            new PenaltyAssignmentStrategy(NormalAction.REVERSE , gameContext.getTurnManager().getNextPlayer(1))
                                        .applyAction(gameContext);
        }
    }
}
