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
        //Player targetPlayer = game.getPlayers().get(game.getNextPlayerIndex(1));
        //Card card = game.peekTopCard(game.getDiscardPile());
        //Action action = ((ActionCard)card).getAction();

        // if there is only 2 players , then reverse becomes a skip ( according to uno rules )
        /*if(action == NormalAction.SKIP || action == NormalAction.REVERSE && game.getNoOfPlayers() == 2)
            targetPlayer.setPenalty(StandardPenalty.SKIP);
        else if(action == NormalAction.DRAW_2)
            targetPlayer.setPenalty(StandardPenalty.DRAW_2);
        else if (action == WildAction.WILD_DRAW_4)
            targetPlayer.setPenalty(StandardPenalty.DRAW_4);*/
        if (targetPlayer == null) targetPlayer = game.getNextPlayer(1);
        targetPlayer.setPenalty(action.getAssociatedPenalty());

    }
}
