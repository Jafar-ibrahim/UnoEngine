package UnoEngine.GameVariations;

import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.InputOutputManager;
import UnoEngine.Player;
import UnoEngine.PlayersManager;
import UnoEngine.Strategies.CardDealingStrategies.CardDealingStrategy;
import UnoEngine.Strategies.StrategyRegistry;

import java.util.*;

public abstract class Game {
    protected String name;
    protected int  pointsToWin;
    protected GameStateManager gameStateManager;
    protected PlayersManager playersManager;
    protected CardManager cardManager;
    protected InputOutputManager inputOutputManager;
    protected TurnManager turnManager;
    protected StrategyRegistry strategyRegistry;
    protected CardDealingStrategy cardDealingStrategy;

    public final void play(){
        cardManager.buildUnoDeck();
        addRequiredStrategies();
        inputOutputManager.greetPlayers(name);
        playersManager.setNoOfPlayers(inputOutputManager.readNoOfPlayers());
        playersManager.instantiatePlayers(inputOutputManager.readPlayersNames());
        cardManager.shuffleDeck();
        decideWhoStarts();
        decideDealingStrategy();
        playRounds();
        announceGameResult();
    }

    protected abstract void addRequiredStrategies();

    /*-----------------------Non-Final Methods--------------------*/
    protected void playRounds() {
        while (gameStateManager.gameIsOngoing()){
            cardManager.InitializeThePiles();
            cardDealingStrategy.dealCards(cardManager,playersManager,4);
            System.out.println("*********** Round " + gameStateManager.getRoundNo() + " started ******************");

            playOneRound();
            announceRoundResult();
            if (gameStateManager.roundIsWon() && playerWon(gameStateManager.getRoundWinner())) {
                gameStateManager.updateGameWinner();
            }
            inputOutputManager.printPointsTable(playersManager.getPlayers());
            gameStateManager.incrementRounds();
            cardManager.reclaimCards(playersManager.getPlayers());
        }
    }

    protected boolean playerHasAnyMatchingCards(Player player){
        for (Card card : player.getCards())
            if(cardManager.cardCanBePlayed(card,player))
                return true;

        return false;
    }

    protected final Card mandatoryDraw(Player currentPlayer , int noCardsToDraw){

        System.out.println("You dont have any card that can be played , enter 0 to draw a card : ");
        int num = inputOutputManager.readIntegerInput(0,0);

        currentPlayer.drawCards(cardManager.giveCards(noCardsToDraw));
        Card drawnCard = cardManager.peekTopCard(currentPlayer.getCards());
        System.out.print(currentPlayer.getName() + " drew a card : \n"+currentPlayer.getNumberOfCards()+" - ");
        drawnCard.print();
        return drawnCard;
    }
    protected void announceRoundResult() {
        Player roundWinner = gameStateManager.getRoundWinner();
        if (gameStateManager.roundIsWon()) {
            System.out.println("--> Round winner is : " + roundWinner.getName());
            roundWinner.addPoints(calcRoundPoints(roundWinner));
            for (Player player : playersManager.getPlayers())
                if (player != roundWinner) player.showCards();
        }else {
            System.out.println("----> Round result is draw !!");
        }
    }
    protected void announceGameResult() {
        if(gameStateManager.gameIsWon()){
            System.out.println("----> Game winner is : " + gameStateManager.getGameWinner().getName() +" !!");
        }else {
            System.out.println("----> Game Result is draw !!");
        }
    }

    protected void checkForUno(Player player){
        if (player.getNumberOfCards() == 1){
            if (!inputOutputManager.timedUnoCall(4000, player))
                processPenalty(StandardPenalty.FORGOT_UNO,player);
        }
    }

    protected void processFirstCardInGame(Card firstCard){

        if(firstCard instanceof NormalActionCard){
            Action action = ((ActionCard) firstCard).getAction();
            // whether it's a penalty or just action , it has to be applied to the first player
            if (action.getAssociatedPenalty() == null)
                processAction(action);
            else
                processPenalty(action.getAssociatedPenalty(),turnManager.getCurrentPlayer());
        }else if(firstCard instanceof WildActionCard /*only wild basically*/) {
            turnManager.getCurrentPlayer().showCards();
            processAction(((ActionCard) firstCard).getAction());
        }
    }

    /*-----------------------Final Methods--------------------*/

    protected final void decideDealingStrategy() {
        setCardDealingStrategy(createDealingStrategy());
    }
    public final boolean playerWon(Player player) {
        return player.getPoints() >= pointsToWin;
    }

    /*-----------------------Abstract Methods--------------------*/
    protected abstract void processAction(Action action);
    protected abstract void processPenalty(Penalty penalty , Player targetPlayer);
    protected abstract DeckBuilder createDeckBuilder() ;
    protected abstract CardDealingStrategy createDealingStrategy();
    protected abstract int calcRoundPoints(Player winner);
    //protected abstract boolean cardCanBePlayed(Card card);
    protected abstract void playOneRound();
    protected abstract void decideWhoStarts();

    /*-----------------------Setters + Getters--------------------*/
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getPointsToWin() {return pointsToWin;}
    public void setPointsToWin(int pointsToWin) {this.pointsToWin = pointsToWin;}
    public CardDealingStrategy getCardDealingStrategy() {return cardDealingStrategy;}
    public void setCardDealingStrategy(CardDealingStrategy cardDealingStrategy) {this.cardDealingStrategy = cardDealingStrategy;}
    public StrategyRegistry getStrategyRegistry() {return strategyRegistry;}

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public PlayersManager getPlayersManager() {
        return playersManager;
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public InputOutputManager getInputOutputManager() {
        return inputOutputManager;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    public void setPlayersManager(PlayersManager playersManager) {
        this.playersManager = playersManager;
    }

    public void setCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    public void setInputOutputManager(InputOutputManager inputOutputManager) {
        this.inputOutputManager = inputOutputManager;
    }

    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
    }

    public void setStrategyRegistry(StrategyRegistry strategyRegistry) {
        this.strategyRegistry = strategyRegistry;
    }
}
