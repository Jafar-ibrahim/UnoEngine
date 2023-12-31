package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

import java.util.Scanner;

public enum WildAction implements Action{
    WILD {
        @Override
        public void applyActionHelper(Game game , Player targetPlayer) {
            System.out.println("Please choose a color to continue(enter the color number):\n" +
                    "1-Blue   2-Green   3-Red   4-Yellow");
            int color = game.readIntegerInput(1,4,new Scanner(System.in)) -1;
            game.setCurrentColor(Color.values()[color]);
            System.out.println("[Action]    Color changed to "+Color.values()[color]);
        }
    },
    WILD_DRAW_4 {
        @Override
        public void applyActionHelper(Game game , Player targetPlayer) {
            targetPlayer.setPenalty(StandardPenalty.DRAW_4);
            WILD.applyActionHelper(game,null);
        }
    };

    @Override
    public void applyAction(Game game , Player targetPlayer) {
        applyActionHelper(game,targetPlayer);
    }
    public abstract void applyActionHelper(Game game , Player targetPlayer);
}
