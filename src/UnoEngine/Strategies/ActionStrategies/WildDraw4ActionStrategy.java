package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.WildAction;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.GameContext;

public class WildDraw4ActionStrategy implements ActionStrategy{
    @Override
    public void applyAction(GameContext gameContext) {
        new PenaltyAssignmentStrategy(WildAction.WILD_DRAW_4).applyAction(gameContext);
        new ChangeColorActionStrategy().applyAction(gameContext);
    }
}
