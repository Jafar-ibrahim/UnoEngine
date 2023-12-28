package Cards;

import Enums.*;

public class NormalActionCard extends ActionCard {

    private NormalAction normalAction;
    public NormalActionCard(Color color, NormalAction normalAction, int value) {
        super(color,value);
        this.normalAction = normalAction;
    }

    @Override
    public NormalAction getAction() {
        return normalAction;
    }

    @Override
    public void setAction(Action action) {
        normalAction = (NormalAction) action;
    }


    /*public boolean matches(Card topCard) {
        if(this.getColor() == topCard.getColor()) return true;
        if(topCard instanceof NormalActionCard)
            return this.getAction() == ((NormalActionCard) topCard).getAction();

        return false;
    }*/

    @Override
    public void print() {
        System.out.println("Action Card   | "+getColor().toString()+" | "+getAction().toString());
    }
}
