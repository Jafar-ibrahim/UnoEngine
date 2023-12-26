package Cards;

import Enums.*;

public class WildActionCard extends ActionCard{

    private final WildAction wildAction;

    public WildActionCard(WildAction wildAction,int value) {
        super(Color.ALL,wildAction,value);
        this.wildAction = wildAction;
    }

    public Action getAction() {
        return wildAction;
    }

    @Override
    public boolean canBePlayed(Card topCard) {
        return false;
    }

    @Override
    public void print() {
        System.out.println("Wild Card | "+ getAction().toString());
    }
}
