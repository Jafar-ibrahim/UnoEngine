import Cards.*;
import Enums.Action;
import Enums.NormalAction;
import Enums.Penalty;
import Enums.WildAction;

public class PenaltyAssignmentStrategy implements ActionsApplicationStrategy{
    @Override
    public void applyAction(Game game) {
        Player targetPlayer = game.getPlayers().get(game.getNextPlayerIndex());
        Card card = game.getTopCard(game.getDiscardPile());
        if(card instanceof ActionCard){
            Action action = ((ActionCard)card).getAction();
            if(action == NormalAction.SKIP)
                targetPlayer.setPenalty(Penalty.SKIP);
            else if(action == NormalAction.DRAW_2)
                targetPlayer.setPenalty(Penalty.DRAW_2);
            else if (action == WildAction.CHANGE_COLOR_AND_DRAW_4)
                targetPlayer.setPenalty(Penalty.DRAW_4);
        }
    }
}
