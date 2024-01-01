package UnoEngine.Enums;

import UnoEngine.Cards.Card;
import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

import java.util.List;
import java.util.Scanner;

public enum AllWildAction implements Action{

    WILD(StandardPenalty.NONE),
    SKIP_2(StandardPenalty.SKIP),
    TARGETED_DRAW_2(StandardPenalty.NONE),
    FORCED_SWAP(StandardPenalty.NONE);

    private Penalty associatedPenalty;

    AllWildAction(Penalty associatedPenalty) {
        this.associatedPenalty = associatedPenalty;
    }

    @Override
    public Penalty getAssociatedPenalty() {
        return associatedPenalty;
    }

    @Override
    public void setAssociatedPenalty(Penalty associatedPenalty) {
        this.associatedPenalty = associatedPenalty;
    }

}
