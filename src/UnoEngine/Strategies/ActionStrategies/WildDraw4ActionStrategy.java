package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Enums.WildAction;
import UnoEngine.GameVariations.Game;

public class WildDraw4ActionStrategy implements ActionStrategy{
    @Override
    public void applyAction(Game game) {
        new PenaltyAssignmentStrategy(WildAction.WILD_DRAW_4).applyAction(game);
        game.getStrategyRegistry().getActionStrategy(WildAction.WILD).applyAction(game);
    }
}
