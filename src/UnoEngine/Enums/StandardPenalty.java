package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public enum StandardPenalty implements Penalty{
    NONE, SKIP , DRAW_2 , DRAW_4 , FORGOT_UNO;

    /*NONE {
        @Override
        public void applyPenaltyHelper(Game game, Player targetPlayer) {

        }
    }, SKIP {
        @Override
        public void applyPenaltyHelper(Game game, Player targetPlayer) {
            System.out.println("[Action]    "+game.getCurrentPlayer().getName()+" turn is skipped ");
            game.advanceTurn();
        }
    }, DRAW_2 {
        @Override
        public void applyPenaltyHelper(Game game, Player targetPlayer) {
            targetPlayer.drawCards(game.giveCards(2,game.getDrawPile()));
            System.out.println("[Action]    "+game.getCurrentPlayer().getName() +" drew 2 cards");
            SKIP.applyPenalty(game,targetPlayer);
        }
    }, DRAW_4 {
        @Override
        public void applyPenaltyHelper(Game game, Player targetPlayer) {
            targetPlayer.drawCards(game.giveCards(4,game.getDrawPile()));
            System.out.println("[Action]    "+game.getCurrentPlayer().getName() +" drew 4 cards");
            SKIP.applyPenalty(game,targetPlayer);
        }
    }, FORGOT_UNO {
        @Override
        public void applyPenaltyHelper(Game game, Player targetPlayer) {
            System.out.println("[Announcement]    Player " + targetPlayer.getName() + " didn't call Uno , Penalty!");
            // Didn't call DRAW_2 because unlike FORGOT_UNO, it skips current player turn
            targetPlayer.drawCards(game.giveCards(2,game.getDrawPile()));
            System.out.println("[Action]    "+game.getCurrentPlayer().getName() +" drew 2 cards");
        }
    };

    @Override
    public void applyPenalty(Game game , Player targetPlayer) {
        applyPenaltyHelper(game,targetPlayer);
    }
    public abstract void applyPenaltyHelper(Game game , Player targetPlayer);*/
}
