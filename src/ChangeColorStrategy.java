import Cards.*;
import Enums.Color;

import java.util.Scanner;

public class ChangeColorStrategy implements ActionsApplicationStrategy {
    @Override
    public void applyAction(Game game,Card card) {
        System.out.print("Please choose a color to continue(enter the color number):\n1-Blue\t2-Green\t3-Red\t4-Yellow");
        int color = game.readIntegerInput(new Scanner(System.in));
        card.setColor(Color.values()[color]);
    }
}
