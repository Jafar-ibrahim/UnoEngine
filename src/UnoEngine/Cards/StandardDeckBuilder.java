package UnoEngine.Cards;

import UnoEngine.Enums.Color;
import UnoEngine.Enums.NormalAction;
import UnoEngine.Enums.WildAction;

import java.util.ArrayList;
import java.util.List;

public class StandardDeckBuilder implements DeckBuilder{

    List<Card> deck;

    public StandardDeckBuilder() {
        deck = new ArrayList<>();
    }

    @Override
    public DeckBuilder buildNumberedCards() {
        /*for(int i = 0; i < Color.values().length-1; i++){
            Color currentColor = Color.values()[i];
            deck.add(new NumberedCard(currentColor,0));
            for(int j = 1 ; j < 10 ; j++){
                deck.add(new NumberedCard(currentColor,j));
                deck.add(new NumberedCard(currentColor,j));
            }
        }*/
        return this;
    }

    @Override
    public DeckBuilder buildNormalActionCards() {
        for(int i = 0 ; i < Color.values().length-1; i++){
            Color currentColor = Color.values()[i];
            for(int j = 0 ; j < 2 ; j++){
                deck.add(new NormalActionCard(currentColor, NormalAction.REVERSE,20));
                deck.add(new NormalActionCard(currentColor, NormalAction.SKIP,20));
                deck.add(new NormalActionCard(currentColor, NormalAction.DRAW_2,20));
            }
        }
        return this;
    }

    @Override
    public DeckBuilder buildWildActionCards() {
        for(int i = 0; i < WildAction.values().length ; i++){
            WildAction currentWildAction = WildAction.values()[i];

            for(int j = 0 ; j < 4 ; j++)
                deck.add(new WildActionCard(currentWildAction,50));
        }
        return this;
    }

    @Override
    public List<Card> getDeck() {
        return this.deck;
    }
}
