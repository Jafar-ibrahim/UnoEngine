package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Cards.Card;
import UnoEngine.Cards.CardManager;
import UnoEngine.Enums.GameState;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;
import UnoEngine.GameVariations.GameStateManager;
import UnoEngine.InputOutputManager;
import UnoEngine.Player;

import java.util.List;
import java.util.Scanner;

public class ForcedSwapStrategy implements ActionStrategy {
    @Override
    public void applyAction(GameContext gameContext) {

        Player CurrentPlayer = gameContext.getTurnManager().getCurrentPlayer();
        System.out.println("Forced swap !! this is the number of cards of each player : ");

        Player targetPlayer = gameContext.getInputOutputManager().readPlayerChoice();

        // swapping
        List<Card> temp = CurrentPlayer.getCards();
        CurrentPlayer.setCards(targetPlayer.getCards());
        targetPlayer.setCards(temp);

        System.out.println("[Action]    "+CurrentPlayer.getName()+" swapped cards with "+targetPlayer.getName()+" .");

        // if last card was a forced swap , then the current player is technically
        // handing the win to another player :)
        GameStateManager gameStateManager = gameContext.getGameStateManager();
        if (temp.isEmpty()){
            gameStateManager.setRoundState(GameState.A_PLAYER_WON);
            gameStateManager.setRoundWinner(targetPlayer);
        }
    }
}
