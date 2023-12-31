package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public interface Penalty {
    // Specifying the target player isn't always needed ,but It's more extensible/future-proof .
    void applyPenalty(Game game , Player targetPlayer);
}
