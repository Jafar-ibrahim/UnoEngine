package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.Color;
import UnoEngine.GameVariations.Game;

import java.util.Scanner;

public class ChangeColorActionStrategy implements ActionsApplicationStrategy {
    @Override
    public void applyAction(Game game) {
        System.out.println("Please choose a color to continue(enter the color number):\n" +
                "1-Blue   2-Green   3-Red   4-Yellow");
        int color = game.readIntegerInput(1,4,new Scanner(System.in)) -1;
        game.setCurrentColor(Color.values()[color]);
        System.out.println("[Action]    Color changed to "+Color.values()[color]);
    }
}
