package UnoEngine.Cards;

import UnoEngine.Enums.Action;
import UnoEngine.Enums.AllWildAction;
import UnoEngine.Enums.NormalAction;
import UnoEngine.Enums.WildAction;

import java.util.ArrayList;
import java.util.List;

public class AllWildDeckBuilder implements DeckBuilder{
    List<Card> deck;

    public AllWildDeckBuilder() {
        deck = new ArrayList<>();
    }

    @Override
    public DeckBuilder buildNumberedCards() {
        return this;
    }

    @Override
    public DeckBuilder buildNormalActionCards() {
        return this;
    }

    @Override
    public DeckBuilder buildWildActionCards() {
        for(int i = 0 ; i < 52 ; i++){
            deck.add(new WildActionCard(AllWildAction.WILD,20));
        }
        for (Action action : NormalAction.values()){
            for(int i = 0 ; i < 8 ; i++){
                deck.add(new WildActionCard(action,50));
            }
        }
        for(AllWildAction allWildAction : AllWildAction.values()){
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
