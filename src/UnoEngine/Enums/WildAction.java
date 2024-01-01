package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

import java.util.Scanner;

public enum WildAction implements Action{

    WILD(StandardPenalty.NONE) ,
    WILD_DRAW_4(StandardPenalty.DRAW_4);
    private Penalty associatedPenalty;

    WildAction(Penalty associatedPenalty) {
        this.associatedPenalty = associatedPenalty;
    }

    public Penalty getAssociatedPenalty() {
        return associatedPenalty;
    }

    public void setAssociatedPenalty(Penalty associatedPenalty) {
        this.associatedPenalty = associatedPenalty;
    }

}
