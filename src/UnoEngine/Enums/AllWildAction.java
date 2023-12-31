package UnoEngine.Enums;

import UnoEngine.Cards.Card;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

import java.util.List;
import java.util.Scanner;

public enum AllWildAction implements Action{
    WILD_DRAW_4 {
        @Override
        void applyActionHelper(Game game){
            game.getNextPlayer(1).setPenalty(StandardPenalty.DRAW_4);
        }
    },WILD {
        @Override
        void applyActionHelper(Game game) {
            // Wild has no action/effect in All Wild!
        }
    }, SKIP_2 {
        @Override
        void applyActionHelper(Game game) {
            game.getNextPlayer(1).setPenalty(StandardPenalty.SKIP);
            // In case of 2 players only , the target player will be skipped for 2 turns
            // ,so I'll have to skip a turn to not skip the current player
            if(game.getNoOfPlayers() == 2 )
                game.getNextPlayer(3).setPenalty(StandardPenalty.SKIP);
            else
                game.getNextPlayer(2).setPenalty(StandardPenalty.SKIP);
        }
    }, TARGETED_DRAW_2 {
        @Override
        void applyActionHelper(Game game) {

            System.out.println("Forced swap !! These are the numbers(indices) of the players : ");
            System.out.println("Enter the number(index) of the player you want to draw 2 cards : ");
            int i = 0;
            for(Player player:game.getPlayers())
                System.out.println(i++ +"- "+player.getName()+" : "+player.getNumberOfCards() +" cards .");

            int targetPlayerIndex = game.readIntegerInput(1, game.getNoOfPlayers(), new Scanner(System.in)) - 1;
            Player targetPlayer = game.getPlayers().get(targetPlayerIndex);
            System.out.println("Player "+targetPlayer.getName() +" was chosen.");
            StandardPenalty.DRAW_2.applyPenalty(game,targetPlayer);
        }
    }, FORCED_SWAP {
        @Override
        void applyActionHelper(Game game) {
            System.out.println("Forced swap !! this is the number of cards of each player : ");
            int i = 1;
            for(Player player:game.getPlayers())
                System.out.println(i++ +"- "+player.getName()+" : "+player.getNumberOfCards() +" cards .");
            System.out.println("Enter the number(index) of the player you want to swap cards with : ");
            int targetPlayerIndex = game.readIntegerInput(1, game.getNoOfPlayers(), new Scanner(System.in)) - 1;
            Player targetPlayer = game.getPlayers().get(targetPlayerIndex);

            List<Card> temp = game.getCurrentPlayer().getCards();
            game.getCurrentPlayer().setCards(targetPlayer.getCards());
            targetPlayer.setCards(temp);

            System.out.println("[Action]    "+game.getCurrentPlayer().getName()+" swapped cards with "+targetPlayer.getName()+" .");
        }
    };

    @Override
    public void applyAction(Game game, Player targetPlayer) {
        applyActionHelper(game);
    }

    abstract void applyActionHelper(Game game);
}
