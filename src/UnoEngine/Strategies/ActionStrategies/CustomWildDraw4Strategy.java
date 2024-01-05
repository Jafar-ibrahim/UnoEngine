package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.WildAction;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;

public class CustomWildDraw4Strategy implements ActionStrategy{
    @Override
    public void applyAction(GameContext gameContext) {
        new PenaltyAssignmentStrategy(WildAction.WILD_DRAW_4).applyAction(gameContext);
    }
}
