package UnoEngine.GameVariations;

import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;
import UnoEngine.Strategies.ActionStrategies.ForcedSwapStrategy;
import UnoEngine.Strategies.ActionStrategies.PenaltyAssignmentStrategy;
import UnoEngine.Strategies.ActionStrategies.ReverseActionStrategy;
import UnoEngine.Strategies.CardDealingStrategies.CardDealingStrategy;
import UnoEngine.Strategies.CardDealingStrategies.StandardCardDealingStrategy;
import UnoEngine.Strategies.PenaltyStrategies.*;

import java.util.Random;
import java.util.Scanner;

public class AllWildUno extends Game{
    public AllWildUno(int pointsToWin , GameDirection gameDirection) {
        super(pointsToWin, gameDirection);
        setName("All Wild! Uno");

    }

    @Override
    protected DeckBuilder createDeckBuilder() {
        return new AllWildDeckBuilder();
    }

    @Override
    protected CardDealingStrategy createDealingStrategy() {
        return new StandardCardDealingStrategy();
    }

    @Override
    public int calcRoundPoints(Player winner) {
        int points = 0;
        for(Player player : getPlayers()){
            if(player != winner)
                for(Card card : player.getCards())
                    points += card.getValue();
        }
        System.out.println("Winner got  : "+points+" points !");
        return points;
    }

    @Override
    protected boolean cardCanBePlayed(Card card) {
        return true;
    }

    @Override
    public void playOneRound() {
        System.out.println();
        InitializeThePlay();
        while (true){
            Player currentPlayer = getCurrentPlayer();
            checkForPenalty();
            if (currentPlayer != getCurrentPlayer()) continue;

            printUserInterface();

            int chosenCardIndex = readCardIndex(new Scanner(System.in)) - 1;
            WildActionCard chosenCard = (WildActionCard) currentPlayer.getCards().get(chosenCardIndex);
            currentPlayer.playCard(chosenCard);
            getDiscardPile().add(chosenCard);
            checkForUno();

            if (currentPlayer.getNumberOfCards() == 0){
                setRoundState(GameState.A_PLAYER_WON);
                return;
            }
            if (chosenCard.getAction() != WildAction.WILD)
                processAction(chosenCard.getAction());

            advanceTurn();
        }
    }
    @Override
    protected void InitializeThePlay(){
        getDrawPile().addAll(getUnoDeck());
        getDiscardPile().clear();
        Card firstCard = drawAndRemoveCard(getDrawPile());

        getDiscardPile().add(firstCard);
        System.out.print("First card put on discard pile is : ");firstCard.print();
        setCurrentColor(firstCard.getColor());
    }
    @Override
    protected void processAction(Action action) {
        action.applyAction(this,getNextPlayer(1));
    }
    @Override
    protected void processPenalty(Penalty penalty) {
        getCurrentPlayer().setPenalty(StandardPenalty.NONE);
        penalty.applyPenalty(this,getCurrentPlayer());
    }
    public void printUserInterface(){
        Card topDiscard = peekTopCard(getDiscardPile());
        System.out.println("------------------ "+getCurrentPlayer().getName()+" turn ------------------");
        System.out.print("Top card on discard pile is : ");topDiscard.print();
        getCurrentPlayer().showCards();
    }


    @Override
    public void decideWhoStarts(){
        Random rand = new Random();
        setCurrentPlayerPosition(rand.nextInt(getNoOfPlayers()));
        System.out.println(getPlayers().get(getCurrentPlayerPosition()).getName() +" starts first ");
    }

    public void checkForPenalty(){
        if(getCurrentPlayer().getPenalty() != StandardPenalty.NONE){
            processPenalty(getCurrentPlayer().getPenalty());
        }
    }
}
