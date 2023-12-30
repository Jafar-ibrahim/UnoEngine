package UnoEngine.Cards;

import UnoEngine.Enums.AllWildAction;

import java.util.ArrayList;
import java.util.List;

public class AllWildDeckBuilder implements DeckBuilder{
    List<Card> deck;

    public AllWildDeckBuilder() {
        deck = new ArrayList<>();
    }

    @Override
    public DeckBuilder buildNumberedCards() {
        return null;
    }

    @Override
    public DeckBuilder buildNormalActionCards() {
        return null;
    }

    @Override
    public DeckBuilder buildWildActionCards() {
        for(int i = 0 ; i < 52 ; i++){
            deck.add(new WildActionCard(AllWildAction.WILD,20));
        }
        for(AllWildAction allWildAction : AllWildAction.values()){
            if (allWildAction != AllWildAction.WILD)
                for(int i = 0 ; i < 8 ; i++){
                    deck.add(new WildActionCard(allWildAction,50));
                }
        }
        return this;
    }

    @Override
    public List<Card> getDeck() {
        return this.deck;
    }
}
