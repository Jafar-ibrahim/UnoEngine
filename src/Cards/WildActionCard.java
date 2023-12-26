package Cards;

import Enums.*;

public class WildActionCard extends Card{

    private final WildAction wildAction;

    public WildActionCard(WildAction wildAction,int value) {
        super(Color.ALL,value);
        this.wildAction = wildAction;
    }

    public WildAction getWildAction() {
        return wildAction;
    }

    @Override
    public boolean canBePlayed(Card topCard) {
        return false;
    }

    @Override
    public void print() {
        System.out.println("Wild Card | "+getWildAction().toString());
    }
}
