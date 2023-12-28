import Cards.*;
import Enums.Color;

import java.util.Scanner;

public class ChangeColorStrategy implements ActionsApplicationStrategy {
    @Override
    public void applyAction(Game game) {
        System.out.println("Please choose a color to continue(enter the color number):\n" +
                "1-Blue   2-Green   3-Red   4-Yellow");
        int color = game.readIntegerInput(new Scanner(System.in)) -1;
        game.getTopCard(game.getDiscardPile()).setColor(Color.values()[color]);
        System.out.println("Color changed to "+Color.values()[color]);
    }
}
