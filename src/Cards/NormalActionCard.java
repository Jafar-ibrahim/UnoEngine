package Cards;

import Enums.*;

public class NormalActionCard extends Card {

    private final Action action;
    public NormalActionCard(Color color, Action action , int value) {
        super(color,value);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public boolean canBePlayed(Card topCard) {
        if(this.getColor() == topCard.getColor()) return true;
        if(topCard instanceof NormalActionCard)
            return this.getAction() == ((NormalActionCard) topCard).getAction();

        return false;
    }

    @Override
    public void print() {
        System.out.println("Action Card   | "+getColor().toString()+" | "+getAction().toString());
    }
}
