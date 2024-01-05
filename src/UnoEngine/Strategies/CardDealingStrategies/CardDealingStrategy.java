package UnoEngine.Strategies.CardDealingStrategies;

import UnoEngine.Cards.CardManager;
import UnoEngine.GameVariations.Game;
import UnoEngine.PlayersManager;

public interface CardDealingStrategy {
    void dealCards(CardManager cardManager, PlayersManager playersManager, int NoOfCardsEach);
}
