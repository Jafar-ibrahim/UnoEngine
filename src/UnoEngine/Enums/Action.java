package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public interface Action {
    // Specifying the target player isn't always needed ,but It's more extensible/future-proof .
    //void applyAction(Game game , Player targetPlayer);
    Penalty getAssociatedPenalty() ;

    void setAssociatedPenalty(Penalty associatedPenalty);
}
