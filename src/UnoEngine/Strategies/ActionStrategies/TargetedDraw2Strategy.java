package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

import java.util.Scanner;

public class TargetedDraw2Strategy implements ActionStrategy{
    @Override
    public void applyAction(Game game) {
        System.out.println("Forced swap !! These are the numbers(indices) of the players : ");
        System.out.println("Enter the number(index) of the player you want to draw 2 cards : ");
        int i = 1;
        for(Player player:game.getPlayers())
            System.out.println(i++ +"- "+player.getName()+" : "+player.getNumberOfCards() +" cards ");

        Player targetPlayer;
        while (true){
            int targetPlayerIndex = game.readIntegerInput(1, game.getNoOfPlayers(), new Scanner(System.in)) - 1;
            targetPlayer = game.getPlayers().get(targetPlayerIndex);
            if (targetPlayer == game.getCurrentPlayer())
                System.out.println("You cant target yourself , please choose another player : ");
            else
                break;
        }

        System.out.println("Player "+targetPlayer.getName() +" was chosen.");
        StandardPenalty.DRAW_2.applyPenalty(game,targetPlayer);
    }
}
