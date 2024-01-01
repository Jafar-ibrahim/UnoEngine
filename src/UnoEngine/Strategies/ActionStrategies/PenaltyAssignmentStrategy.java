package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.Cards.*;
import UnoEngine.Enums.Action;
import UnoEngine.Enums.NormalAction;
import UnoEngine.Enums.StandardPenalty;
import UnoEngine.Enums.WildAction;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public class PenaltyAssignmentStrategy implements ActionStrategy {

    Action action;
    Player targetPlayer;

    public PenaltyAssignmentStrategy(Action action) {
        this.action = action;
    }

    public PenaltyAssignmentStrategy(Action action , Player targetPlayer) {
        this.action = action;
        this.targetPlayer = targetPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void applyAction(Game game) {
        if (targetPlayer == null) targetPlayer = game.getNextPlayer(1);
        targetPlayer.setPenalty(action.getAssociatedPenalty());
    }
}
