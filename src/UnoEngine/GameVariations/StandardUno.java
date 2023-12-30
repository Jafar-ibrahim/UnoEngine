package UnoEngine.GameVariations;
import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;
import UnoEngine.Strategies.CardDealingStrategies.CardDealingStrategy;
import UnoEngine.Strategies.CardDealingStrategies.StandardCardDealingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class StandardUno extends Game{
    public StandardUno() {
        super();
    }

    @Override
    protected DeckBuilder createDeckBuilder() {
        return new StandardDeckBuilder();
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
    public void resetCards() {
        for(Player player : getPlayers())
            getUnoDeck().addAll(player.getCards());
        getUnoDeck().addAll(getDiscardPile());
        getUnoDeck().addAll(getDrawPile());
    }
    @Override
    public boolean cardCanBePlayed(Card card){
        Card topDiscard = peekTopCard(getDiscardPile());
        if(card instanceof WildActionCard){
            if (((WildActionCard) card).getAction() == WildAction.CHANGE_COLOR_AND_DRAW_4){
                for (Card otherCard : getCurrentPlayer().getCards())
                    // wild+4 can be only played if no other card can be played
                    if (otherCard != card && cardCanBePlayed(otherCard))
                        return false;
            }
            return true;
        }else{
            if(card.getColor() == topDiscard.getColor()) return true;
            else if (card instanceof NormalActionCard && topDiscard instanceof NormalActionCard) {
                return ((NormalActionCard) card).getAction() == ((NormalActionCard) topDiscard).getAction();
            }else if(card instanceof NumberedCard){
                return card.getValue() == topDiscard.getValue();
            }
        }
        return false;
    }
    @Override
    public void playOneRound() {
        InitializeThePlay();
        while (true){
            Player currentPlayer = getCurrentPlayer();
            checkForPenalty();
            if (currentPlayer != getCurrentPlayer()) continue;

            printUserInterface();
            if(!playerHasAnyMatchingCards(currentPlayer)){
                mandatoryDraw(currentPlayer,1);
                if (!cardCanBePlayed(peekTopCard(getCurrentPlayer().getCards())))
                    {advanceTurn();continue;}
            }
            int chosenCardIndex = readCardIndex(new Scanner(System.in)) - 1;
            Card chosenCard = currentPlayer.getCards().get(chosenCardIndex);
            while(!cardCanBePlayed(chosenCard)){
                System.out.println("This card cannot be played ");
                chosenCard = currentPlayer.getCards().get(readCardIndex(new Scanner(System.in)) - 1);
            }
            currentPlayer.playCard(chosenCard);
            getDiscardPile().add(chosenCard);
            checkForUno();

            if (currentPlayer.getNumberOfCards() == 0){
                setRoundState(GameState.A_PLAYER_WON);
                return;
            }
            if(chosenCard instanceof ActionCard){
                processAction(((ActionCard)chosenCard).getAction());
            }
            advanceTurn();
        }
    }

    public void printUserInterface(){
        Card topDiscard = peekTopCard(getDiscardPile());
        System.out.println("------------------ "+getCurrentPlayer().getName()+" turn ------------------");
        System.out.print("Top card on discard pile is : ");topDiscard.print();
        getCurrentPlayer().showCards();
    }
    public void checkForPenalty(){
        if(getCurrentPlayer().getPenalty() != Penalty.NONE){
            processPenalty(getCurrentPlayer().getPenalty());
        }
    }
    public void checkForUno(){
        if (getCurrentPlayer().getNumberOfCards() == 1){
            if (!saidUno(4000))
                processPenalty(Penalty.FORGOT_UNO);
        }
    }

    @Override
    public void decidePointsToWin() {
        setPointsToWin(1);
    }
    @Override
    public void decideInitialGameDirection() {
        setGameDirection(GameDirection.CLOCKWISE);
    }
    @Override
    public void decideWhoStarts(){
        int max = 0;
        for(int i = 0 ; i < getNoOfPlayers() ; i++){
            Card topCard = getUnoDeck().get(getUnoDeck().size()-1-i);
            if (topCard instanceof NumberedCard) {
                if (topCard.getValue() > max){
                    max = topCard.getValue();
                    setCurrentPlayerPosition(i);
                }
            }// any other card type (action/wild) is valued 0
        }
        shuffleCards(getUnoDeck());
        System.out.println(getPlayers().get(getCurrentPlayerPosition()).getName() +" starts first ");
    }
}
