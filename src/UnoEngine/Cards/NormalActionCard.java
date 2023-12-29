package UnoEngine.Cards;

import UnoEngine.Enums.*;

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
    @Override
    public void print() {
        System.out.println("Action Card   | "+getColor().toString()+" | "+getAction().toString());
    }
}
