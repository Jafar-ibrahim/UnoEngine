import Enums.Penalty;

public class Draw4Strategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        Player targetPlayer = game.getPlayers().get(game.getCurrentPlayerPosition());
        targetPlayer.drawCards(game.giveCards(4,game.getDrawPile()));
        System.out.println(game.getCurrentPlayer().getName() +" drew 4 cards");
        game.processPenalty(Penalty.SKIP);
    }
}
