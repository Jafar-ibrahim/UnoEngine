package Cards;

import Enums.*;

public class WildActionCard extends ActionCard{

    private WildAction wildAction;

    public WildActionCard(WildAction wildAction,int value) {
        super(Color.ALL,value);
        this.wildAction = wildAction;
    }

    @Override
    public Action getAction() {
        return wildAction;
    }

    @Override
    public void setAction(Action action) {
        wildAction = (WildAction) action;
    }


    /*public boolean matches(Card topCard) {
        if(wildAction == WildAction.CHANGE_COLOR) return true;
        else return
    }*/

    @Override
    public void print() {
        if (getColor() == Color.ALL)
            System.out.println("Wild Card     | "+ getAction().toString());
        else
            System.out.println("Wild Card     | "+ getColor());
    }
}
