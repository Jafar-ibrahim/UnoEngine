package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public enum NormalAction implements Action {

    SKIP(StandardPenalty.SKIP)
    // In some cases reverse action becomes a skip penalty to the other player.
    , REVERSE(StandardPenalty.SKIP)
    , DRAW_2(StandardPenalty.DRAW_2);

    private Penalty associatedPenalty;
    NormalAction(Penalty associatedPenalty) {
        this.associatedPenalty = associatedPenalty;
    }
    public Penalty getAssociatedPenalty() {
        return associatedPenalty;
    }
    public void setAssociatedPenalty(Penalty associatedPenalty) {
        this.associatedPenalty = associatedPenalty;
    }
}
