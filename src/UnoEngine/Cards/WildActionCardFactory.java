package UnoEngine.Cards;

import UnoEngine.Enums.Action;
import UnoEngine.Enums.Color;
import UnoEngine.Enums.WildAction;

public class WildActionCardFactory implements CardFactory{
    @Override
    public Card createCard(int value, Color color, Action action) {
        return new WildActionCard((WildAction) action,value);
    }
}
