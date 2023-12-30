package UnoEngine.Cards;

import java.util.List;

public interface DeckBuilder {
    DeckBuilder buildNumberedCards();
    DeckBuilder buildNormalActionCards();
    DeckBuilder buildWildActionCards();

    List<Card> getDeck();

}
