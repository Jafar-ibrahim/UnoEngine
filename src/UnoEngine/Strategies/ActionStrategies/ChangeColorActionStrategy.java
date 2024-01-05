package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.Color;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;

import java.util.Scanner;

public class ChangeColorActionStrategy implements ActionStrategy {
    @Override
    public void applyAction(GameContext gameContext) {
        int color = gameContext.getInputOutputManager().readColorChoice();
        gameContext.getGameStateManager().setCurrentColor(Color.values()[color]);
        System.out.println("[Action]    Color changed to "+Color.values()[color]);
    }
}
