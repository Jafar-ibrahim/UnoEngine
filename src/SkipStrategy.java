import Enums.Penalty;

public class SkipStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        System.out.println(game.getCurrentPlayer().getName()+" turn is skipped ");
        game.advanceTurn();
    }
}
