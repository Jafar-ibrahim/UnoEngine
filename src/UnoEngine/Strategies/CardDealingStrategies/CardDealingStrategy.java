package UnoEngine.Strategies.CardDealingStrategies;

import UnoEngine.GameVariations.Game;

public interface CardDealingStrategy {
    void dealCards(Game game, int NoOfCardsEach);
}
