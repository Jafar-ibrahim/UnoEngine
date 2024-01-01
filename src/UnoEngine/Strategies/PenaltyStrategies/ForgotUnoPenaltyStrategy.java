package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public class ForgotUnoPenaltyStrategy implements PenaltyStrategy {
    @Override
    public void applyPenalty(Game game, Player targetPlayer) {

        System.out.println("[Announcement]    Player " + targetPlayer.getName() + " didn't call Uno , Penalty!");
        // Didn't call DRAW_2 because unlike FORGOT_UNO, it skips current player turn
        targetPlayer.drawCards(game.giveCards(2,game.getDrawPile()));
        System.out.println("[Action]    "+targetPlayer.getName() +" drew 2 cards");

    }
}
