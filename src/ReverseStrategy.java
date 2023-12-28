import Cards.Card;
import Enums.GameDirection;
import Enums.Penalty;

public class ReverseStrategy implements ActionsApplicationStrategy{
    @Override
    public void applyAction(Game game) {
        if(game.getGameDirection() == GameDirection.CLOCKWISE)
            game.setGameDirection(GameDirection.COUNTER_CLOCKWISE);
        else
            game.setGameDirection(GameDirection.CLOCKWISE);

        System.out.println("Game direction got reversed");
        // if there is only 2 players , then reverse becomes a skip ( according to uno rules )
        if (game.getNoOfPlayers() == 2) game.processPenalty(Penalty.SKIP);
    }
}
