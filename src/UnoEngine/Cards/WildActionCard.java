package UnoEngine.Cards;

import UnoEngine.Enums.*;

public class WildActionCard extends ActionCard{

    private Action wildAction;

    public WildActionCard(Action wildAction,int value) {
        super(Color.WILD_COLOR,value);
        this.wildAction = wildAction;
    }
    @Override
    public Action getAction() {
        return wildAction;
    }
    @Override
    public void setAction(Action action) {
        wildAction = action;
    }
    @Override
    public void print() {
        System.out.println("Wild Card     | "+ getAction().toString());
    }
}
