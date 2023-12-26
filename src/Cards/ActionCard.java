package Cards;

import Enums.Action;
import Enums.Color;
import Enums.NormalAction;

public abstract class ActionCard extends Card{

    Action action;
    public ActionCard(Color color, Action action, int value) {
        super(color,value);

    }

    public abstract Action getAction();
    public abstract void setAction();

}