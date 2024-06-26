package UnoEngine.Cards;

import UnoEngine.Enums.Action;
import UnoEngine.Enums.Color;

public abstract class ActionCard extends Card{
    public ActionCard(Color color, int value) {
        super(color,value);
    }
    public abstract Action getAction();
    public abstract void setAction(Action action);

}
