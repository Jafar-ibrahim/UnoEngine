package UnoEngine.Cards;

import UnoEngine.Enums.Action;
import UnoEngine.Enums.Color;
import UnoEngine.Enums.NormalAction;

public class NormalActionCardFactory implements CardFactory {
    @Override
    public Card createCard(int value, Color color, Action action) {
        return new NormalActionCard(color, (NormalAction) action,value);
    }
}
