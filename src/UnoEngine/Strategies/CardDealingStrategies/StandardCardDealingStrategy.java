package UnoEngine.Strategies.CardDealingStrategies;

import UnoEngine.GameVariations.Game;

public class StandardCardDealingStrategy implements CardDealingStrategy {
    @Override
    public void dealCards(Game game, int NoOfCardsEach) {
        for(int i = 0 ; i < NoOfCardsEach ; i++){
            for(int j = 0 ; j < game.getNoOfPlayers() ; j++){
                game.getCurrentPlayer().drawCards(game.giveCards(1,game.getUnoDeck()));
                game.advanceTurn();
            }
        }
    }
}
