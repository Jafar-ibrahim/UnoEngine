public class ForgotUnoStrategy implements PenaltiesApplicationStrategy{
    @Override
    public void applyPenalty(Game game) {
        game.getCurrentPlayer().drawCards(game.giveCards(2,game.getDrawPile()));
    }
}
