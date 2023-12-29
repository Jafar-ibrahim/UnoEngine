package UnoEngine.GameVariations;
import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class StandardUno extends Game{
    public StandardUno() {
        super();
    }
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
    public void resetCards() {
        for(Player player : getPlayers())
            getUnoDeck().addAll(player.getCards());
        getUnoDeck().addAll(getDiscardPile());
        getUnoDeck().addAll(getDrawPile());
    }
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
    public void playOneRound() {
        InitializeThePlay();
        while (true){
            Player currentPlayer = getCurrentPlayer();
            checkForPenalty();
            if (currentPlayer != getCurrentPlayer()) continue;

            printUserInterface();
            if(!playerHasAnyMatchingCards(currentPlayer)){
                boolean drawnCanBePlayed = emergencyDraw(currentPlayer,1);
                if (!drawnCanBePlayed) {advanceTurn();continue;}
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
    public void InitializeThePlay() {
        getDrawPile().addAll(getUnoDeck());
        getDiscardPile().clear();
        Card firstCard = drawAndRemoveCard(getDrawPile());

        // First card can't be Wild +4
        while(firstCard instanceof WildActionCard && ((WildActionCard)firstCard).getAction() == WildAction.CHANGE_COLOR_AND_DRAW_4 ){
            getDrawPile().add(firstCard);
            shuffleCards(getDrawPile());
            firstCard = drawAndRemoveCard(getDrawPile());
        }
        getDiscardPile().add(firstCard);
        System.out.print("First card put on discard pile is : ");firstCard.print();

        // if first card is action card , apply it to the first player
        if(firstCard instanceof NormalActionCard){
            // in case of potential penalty :
            // to assign the penalty to the current player, I have to backtrack by one player
            // cause the method flags the next player with a penalty (not the current one)
            setCurrentPlayerPosition(Math.floorMod(getCurrentPlayerPosition() -1 , getNoOfPlayers()));
            processAction(((ActionCard) firstCard).getAction());
            setCurrentPlayerPosition(Math.floorMod(getCurrentPlayerPosition() +1 ,getNoOfPlayers()));
        }else if(firstCard instanceof WildActionCard /*only wild basically*/) {
            processAction(((ActionCard) firstCard).getAction());
        }
    }
    public void decidePointsToWin() {
        setPointsToWin(1);
    }
    public void decideInitialGameDirection() {
        setGameDirection(GameDirection.CLOCKWISE);
    }
    public void dealCards(int noOfCardsEach) {
        for(int i = 0 ; i < noOfCardsEach ; i++){
            for(int j = 0 ; j < getNoOfPlayers() ; j++){
                getCurrentPlayer().drawCards(giveCards(1,getUnoDeck()));
                advanceTurn();
            }
        }
    }
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

    public List<Card> addNumberedCards(){
        List<Card> numberedCards = new ArrayList<>();

        for(int i = 0; i < Color.values().length-1; i++){
            Color currentColor = Color.values()[i];
            numberedCards.add(new NumberedCard(currentColor,0));
            for(int j = 1 ; j < 10 ; j++){
                numberedCards.add(new NumberedCard(currentColor,j));
                numberedCards.add(new NumberedCard(currentColor,j));
            }
        }
        return numberedCards;
    }
    public  List<Card> addNormalActionCards(){
        List<Card> NormalActionCards = new ArrayList<>();
        for(int i = 0 ; i < Color.values().length-1; i++){
            Color currentColor = Color.values()[i];
            for(int j = 0 ; j < 2 ; j++){
                NormalActionCards.add(new NormalActionCard(currentColor, NormalAction.REVERSE,20));
                NormalActionCards.add(new NormalActionCard(currentColor, NormalAction.SKIP,20));
                NormalActionCards.add(new NormalActionCard(currentColor, NormalAction.DRAW_2,20));
            }
        }
        return NormalActionCards;
    }
    public  List<Card> addWildActionCards(){
        List<Card> WildActionCards = new ArrayList<>();
        for(int i = 0; i < WildAction.values().length ; i++){
            WildAction currentWildAction = WildAction.values()[i];

            for(int j = 0 ; j < 4 ; j++)
                WildActionCards.add(new WildActionCard(currentWildAction,50));
        }
        return WildActionCards;
    }
}
