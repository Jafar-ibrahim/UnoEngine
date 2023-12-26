import Enums.Penalty;

public class SkipStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        game.advanceTurn();
    }
}
