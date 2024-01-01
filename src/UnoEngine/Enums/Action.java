package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public interface Action {
    Penalty getAssociatedPenalty() ;
    // didn't use it in my code , but might be helpful for others to
    // change the Action-Penalty mapping without creating a new enum class
    void setAssociatedPenalty(Penalty associatedPenalty);
}
