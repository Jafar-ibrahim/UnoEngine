package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Cards.CardManager;
import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;
import UnoEngine.Player;

public class ForgotUnoPenaltyStrategy implements PenaltyStrategy {
    @Override
    public void applyPenalty(GameContext gameContext, Player targetPlayer) {
        CardManager cardManager = gameContext.getCardManager();

        System.out.println("[Announcement]    Player " + targetPlayer.getName() + " didn't call Uno , Penalty!");
        // Didn't call DRAW_2 because unlike FORGOT_UNO, it skips current player turn
        targetPlayer.drawCards(cardManager.giveCards(2));
        System.out.println("[Action]    "+targetPlayer.getName() +" drew 2 cards");

    }
}
