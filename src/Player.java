import Cards.Card;
import Enums.Penalty;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int points;
    Penalty penalty;
    boolean uno;
    private ArrayList<Card> cards;

    Player(String name) {
        this.name = name;
        points = 0;
        penalty = Penalty.NONE;
        cards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }
    public int getNumberOfCards() {
        return this.cards.size();
    }
    public boolean hasMatchingCard(Card topCard){
        boolean answer = false;
        for(Card playerCard : cards){
            answer |= playerCard.canBePlayed(topCard);
        }
        return answer;
    }

    public void drawCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
    public Card playCard(int cardIndex){
        Card playedCard = cards.get(cardIndex);
        cards.remove(cardIndex);
        return playedCard;
    }
    public void showCards() {
        System.out.println("Player ("+getName()+")'s cards : ");
        for (int i = 0; i < cards.size(); i++) {
            System.out.print((i + 1) + " - ");
            cards.get(i).print();
        }
    }

    public Penalty getPenalty() {
        return penalty;
    }

    public void setPenalty(Penalty penalty) {
        this.penalty = penalty;
    }

    public boolean isUno() {
        return uno;
    }

    public void setUno(boolean uno) {
        this.uno = uno;
    }
}
