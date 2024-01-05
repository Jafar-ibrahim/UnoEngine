package UnoEngine.Cards;

import UnoEngine.Enums.WildAction;
import UnoEngine.GameVariations.GameStateManager;
import UnoEngine.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardManager {
    private List<Card> unoDeck, drawPile, discardPile;
    private DeckBuilder deckBuilder;
    private GameStateManager gameStateManager;

    private static CardManager instance;

    private CardManager() {
        unoDeck = new ArrayList<>();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        deckBuilder = new StandardDeckBuilder();
        gameStateManager = GameStateManager.getInstance();
    }
    public static CardManager getInstance(){
        if (instance == null)
            instance = new CardManager();
        return instance;
    }

    public final void buildUnoDeck() {
        List<Card> deck =
                deckBuilder
                        .buildNumberedCards()
                        .buildNormalActionCards()
                        .buildWildActionCards()
                        .getDeck();
        setUnoDeck(deck);
    }
    public Card peekTopCard(List<Card> cards){
        return cards.get(cards.size()-1);
    }
    public Card peekTopDiscardPile(){
        return discardPile.get(discardPile.size()-1);
    }
    public Card peekTopDrawPile(){
        return drawPile.get(drawPile.size()-1);
    }
    public boolean cardCanBePlayed(Card card  , Player player){
        Card topDiscard = peekTopCard(getDiscardPile());
        if(card instanceof WildActionCard){
            if (((WildActionCard) card).getAction() == WildAction.WILD_DRAW_4){
                for (Card otherCard : player.getCards()) {
                    if (otherCard instanceof WildActionCard)
                        continue;
                    // wild+4 can be only played if no colored card can be played
                    if (cardCanBePlayed(otherCard,player))
                        return false;
                }
            }
            return true;
        }else{
            if(card.getColor() == gameStateManager.getCurrentColor()) return true;
            else if (card instanceof NormalActionCard && topDiscard instanceof NormalActionCard) {
                return ((NormalActionCard) card).getAction() == ((NormalActionCard) topDiscard).getAction();
            }else if(card instanceof NumberedCard){
                return card.getValue() == topDiscard.getValue();
            }
        }
        return false;
    }
    public void InitializeThePiles() {
        drawPile.addAll(unoDeck);
        discardPile.clear();
        Card firstCard = drawAndRemoveCard(drawPile);

        // First card can't be Wild +4 In standard UNO
        while(!canBeFirstCard(firstCard)){
            drawPile.add(firstCard);
            Collections.shuffle(drawPile);
            firstCard = drawAndRemoveCard(drawPile);
        }
        discardCard(firstCard);
        System.out.print("First card put on discard pile is : ");firstCard.print();
        gameStateManager.setCurrentColor(firstCard.getColor());

    }

    public void discardCard(Card card){
        getDiscardPile().add(card);
    }
    public boolean canBeFirstCard(Card card){
        return !(card instanceof WildActionCard && ((WildActionCard)card).getAction() == WildAction.WILD_DRAW_4);
    }
    public Card drawAndRemoveCard(List<Card> cards){
        if(cards.isEmpty()) reshuffleDiscardPile();

        Card topCard = cards.get(cards.size()-1);
        cards.remove(cards.size()-1);
        return topCard;
    }
    public Card drawAndRemoveCard(){
        if(drawPile.isEmpty()) reshuffleDiscardPile();

        Card topCard = drawPile.get(drawPile.size()-1);
        drawPile.remove(drawPile.size()-1);
        return topCard;
    }

    protected void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            // Not enough cards to reshuffle;declare a draw.
            //setRoundState(GameState.DRAW);
            return;
        }
        // Keep the top card
        Card topCard = discardPile.remove(0);
        Collections.shuffle(discardPile);

        drawPile.addAll(discardPile);
        discardPile.clear();
        discardPile.add(topCard);
    }
    public final List<Card> giveCards(int noOfCards) {
        List<Card> givenCards = new ArrayList<>();
        for (int i = 0; i < noOfCards; i++) {
            givenCards.add(drawAndRemoveCard(drawPile));
        }
        return givenCards;
    }

    public final void shuffleDeck() {
        Collections.shuffle(unoDeck);
    }

    public void reclaimCards(List<Player> players) {
        for(Player player : players)
            unoDeck.addAll(player.getCards());
        unoDeck.addAll(discardPile);
        unoDeck.addAll(drawPile);
    }

    public List<Card> getUnoDeck() {
        return unoDeck;
    }

    public void setUnoDeck(List<Card> unoDeck) {
        this.unoDeck = unoDeck;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(List<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public void setDeckBuilder(DeckBuilder deckBuilder) {
        this.deckBuilder = deckBuilder;
    }
}
