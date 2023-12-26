package Cards;

import Enums.*;

public class NormalActionCard extends ActionCard {

    private final NormalAction normalAction;
    public NormalActionCard(Color color, NormalAction normalAction, int value) {
        super(color,normalAction,value);
        this.normalAction = normalAction;
    }

    public NormalAction getAction() {
        return normalAction;
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
