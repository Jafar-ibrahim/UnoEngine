package UnoEngine;

import UnoEngine.Cards.Card;
import UnoEngine.Enums.Penalty;
import UnoEngine.Enums.StandardPenalty;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int points;
    Penalty penalty;
    private List<Card> cards;

    public Player(String name) {
        this.name = name;
        points = 0;
        penalty = StandardPenalty.NONE;
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
    public void addPoints(int points) {
        this.points += points;
    }
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getNumberOfCards() {
        return this.cards.size();
    }
    public void drawCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
    public void playCard(Card card){
        cards.remove(card);
    }
    public void showCards() {
        System.out.println("Player ("+getName()+")'s cards : ");
        if (cards.isEmpty()) {
            System.out.print(" N/A");
        }else
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
}
