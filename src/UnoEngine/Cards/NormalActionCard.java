package UnoEngine.Cards;

import UnoEngine.Enums.*;

public class NormalActionCard extends ActionCard {

    private Action normalAction;
    public NormalActionCard(Color color, Action normalAction, int value) {
        super(color,value);
        this.normalAction = normalAction;
    }
    @Override
    public Action getAction() {
        return normalAction;
    }
    @Override
    public void setAction(Action action) {
        normalAction = action;
    }
    @Override
    public void print() {
        System.out.println("Action Card   | "+getColor().toString()+" | "+getAction().toString());
    }
}
