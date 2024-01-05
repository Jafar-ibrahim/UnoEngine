package UnoEngine.Strategies.PenaltyStrategies;

import UnoEngine.Cards.CardManager;
import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;
import UnoEngine.Player;

public class Draw2PenaltyStrategy implements PenaltyStrategy {
    @Override
    public void applyPenalty(GameContext gameContext, Player targetPlayer) {
        CardManager cardManager = gameContext.getCardManager();

        targetPlayer.drawCards(cardManager.giveCards(2));
        System.out.println("[Action]    " + targetPlayer.getName() + " drew 2 cards");
        new SkipPenaltyStrategy().applyPenalty(gameContext,targetPlayer);
    }
}
