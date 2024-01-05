package UnoEngine.Strategies.CardDealingStrategies;

import UnoEngine.Cards.CardManager;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;
import UnoEngine.PlayersManager;

public class StandardCardDealingStrategy implements CardDealingStrategy {

    @Override
    public void dealCards(CardManager cardManager, PlayersManager playersManager, int NoOfCardsEach) {
        for(int i = 0 ; i < NoOfCardsEach ; i++){
            for (Player player : playersManager.getPlayers()){
                player.drawCards(cardManager.giveCards(1));
            }
        }
    }
}
