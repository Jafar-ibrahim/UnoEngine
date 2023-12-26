import Cards.Card;
import Enums.GameDirection;

public class ReverseStrategy implements ActionsApplicationStrategy{
    @Override
    public void applyAction(Game game, Card card) {
        if(game.getGameDirection() == GameDirection.CLOCKWISE)
            game.setGameDirection(GameDirection.COUNTER_CLOCKWISE);
        else
            game.setGameDirection(GameDirection.CLOCKWISE);
    }
}
