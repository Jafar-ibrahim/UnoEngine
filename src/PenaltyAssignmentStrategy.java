import Cards.Card;
import Cards.NormalActionCard;
import Cards.WildActionCard;
import Enums.Action;
import Enums.Penalty;
import Enums.WildAction;

public class PenaltyAssignmentStrategy implements ActionsApplicationStrategy{
    @Override
    public void applyAction(Game game, Card card) {
        Player targetPlayer = game.getPlayers().get(game.getNextPlayerIndex());
        if(card instanceof  NormalActionCard){
            Action action = ((NormalActionCard)card).getAction();
            if(action == Action.SKIP)
                targetPlayer.setPenalty(Penalty.SKIP);
            else if(action == Action.DRAW_2)
                targetPlayer.setPenalty(Penalty.DRAW_2);
        }else if(card instanceof WildActionCard){
            WildAction wildAction = ((WildActionCard)card).getWildAction();
            if(wildAction == WildAction.CHANGE_COLOR_AND_DRAW_4)
                targetPlayer.setPenalty(Penalty.DRAW_4);
        }
    }
}
