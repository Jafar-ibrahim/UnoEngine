package UnoEngine.Cards;

import UnoEngine.Enums.Action;
import UnoEngine.Enums.Color;

public interface CardFactory {
    Card createCard(int value,Color color, Action action);
}
