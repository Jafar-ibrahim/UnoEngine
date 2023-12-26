import Enums.Penalty;

public class Draw2Strategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        Player targetPlayer = game.getPlayers().get(game.getCurrentPlayerPosition());
        targetPlayer.drawCards(game.giveCards(2,game.getDrawPile()));
    }
}
