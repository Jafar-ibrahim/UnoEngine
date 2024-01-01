package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.GameDirection;
import UnoEngine.Enums.NormalAction;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;

public class ReverseActionStrategy implements ActionStrategy {
    @Override
    public void applyAction(Game game) {
        if(game.getGameDirection() == GameDirection.CLOCKWISE)
            game.setGameDirection(GameDirection.COUNTER_CLOCKWISE);
        else
            game.setGameDirection(GameDirection.CLOCKWISE);

        System.out.println("[Action]    Game direction got reversed");

        // In case the number of players is 2 , reverse action becomes a skip penalty to the other player
        if(game.getNoOfPlayers() == 2 ){
            new PenaltyAssignmentStrategy(NormalAction.REVERSE,game.getNextPlayer(1)).applyAction(game);
        }
    }
}
