package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Cards.Card;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ForcedSwapStrategy implements ActionsApplicationStrategy{
    @Override
    public void applyAction(Game game) {
        System.out.println("Forced swap !! this is the number of cards of each player : ");
        int i = 1;
        for(Player player:game.getPlayers())
            System.out.println(i+"- "+player.getName()+" : "+player.getNumberOfCards() +"cards.");
        System.out.println("Enter the number(index) of the player you want to swap cards with : ");
        int targetPlayerIndex = game.readIntegerInput(1, game.getNoOfPlayers(), new Scanner(System.in));
        Player targetPlayer = game.getPlayers().get(targetPlayerIndex);

        List<Card> temp = game.getCurrentPlayer().getCards();
        game.getCurrentPlayer().setCards(targetPlayer.getCards());
        targetPlayer.setCards(temp);

        System.out.println("[Action]    "+game.getCurrentPlayer().getName()+" swapped cards with "+targetPlayer.getName()+" .");
    }
}
