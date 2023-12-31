package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.GameDirection;
import UnoEngine.GameVariations.Game;

public class ReverseActionStrategy implements ActionsApplicationStrategy{
    @Override
    public void applyAction(Game game) {
        if(game.getGameDirection() == GameDirection.CLOCKWISE)
            game.setGameDirection(GameDirection.COUNTER_CLOCKWISE);
        else
            game.setGameDirection(GameDirection.CLOCKWISE);

        System.out.println("[Action]    Game direction got reversed");
    }
}
