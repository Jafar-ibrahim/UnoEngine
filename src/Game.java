import Cards.*;
import Enums.*;

import java.util.*;

public abstract class Game {
    private List<Card> unoDeck, drawPile, discardPile;
    private int noOfPlayers , pointsToWin;
    private List<Player> players;
    private int currentPlayerPosition;
    private GameDirection gameDirection;
    private GameState gameState;
    private ActionsApplicationStrategy actionsApplicationStrategy;
    private PenaltiesApplicationStrategy penaltiesApplicationStrategy;

    public Game() {
        unoDeck = new ArrayList<>();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
    }

    public final void play(){
        buildUnoDeck();
        instantiatePlayers();
        shuffleCards(unoDeck);
        decidePointsToWin();
        decideWhoStarts();
        decideInitialGameDirection();
        dealCards();

    }

    public void playRounds() {
        while (!gameIsOver()){
            playOneRound();
        }
    }

    public int playOneRound() {
        while (!RoundIsOver()){
            Player currentPlayer = players.get(currentPlayerPosition);
            if(currentPlayer.penalty != Penalty.NONE){
                processPenalty(currentPlayer.penalty);
                advanceTurn();
                continue;
            }
            currentPlayer.showCards();
            int chosenCardIndex = readCardNumber(new Scanner(System.in));
            discardPile.add(currentPlayer.playCard(chosenCardIndex));
        }
    }

    public void processPenalty(Penalty penalty) {
        if(penalty == Penalty.DRAW_2){
            setPenaltiesApplicationStrategy(new Draw2Strategy());
            penaltiesApplicationStrategy.applyPenalty(this);
        }else if(penalty == Penalty.DRAW_4){
            setPenaltiesApplicationStrategy(new Draw4Strategy());
            penaltiesApplicationStrategy.applyPenalty(this);
        }
    }

    public boolean gameIsOver() {
        for (Player player : players){
            if(player.getPoints() >= pointsToWin)
                return true;
        }
        return false;
    }
    public boolean RoundIsOver() {
        for (Player player : players){
            if(player.getCards().isEmpty())
                return true;
        }
        return false;
    }
    public void decidePointsToWin() {
        setPointsToWin(250);
    }

    public void decideInitialGameDirection() {
        setGameDirection(GameDirection.CLOCKWISE);
    }

    public void dealCards() {
        int noOfCardsEach = 7;
        for(int i = 0 ; i < noOfCardsEach ; i++){
            for(int j = 0 ; j < noOfPlayers ; j++){
                System.out.println(currentPlayerPosition);
                Player currentPlayer = players.get(currentPlayerPosition);
                currentPlayer.drawCards(giveCards(1,unoDeck));
                advanceTurn();
            }
        }
    }

    public int getNextPlayerIndex(){
        if(gameDirection == GameDirection.CLOCKWISE)
            return Math.floorMod(currentPlayerPosition - 1 , noOfPlayers);
        else
            return Math.floorMod(currentPlayerPosition + 1 ,  noOfPlayers);
    }
    public void advanceTurn(){
        currentPlayerPosition = getNextPlayerIndex();
    }

    public void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            // Not enough cards to reshuffle;declare a draw.
            setGameState(GameState.DRAW);
            return;
        }
        // Keep the top card
        Card topCard = discardPile.remove(0);
        Collections.shuffle(discardPile);

        drawPile.addAll(discardPile);
        discardPile.clear();
        discardPile.add(topCard);
    }
    public Card getTopCard(List<Card> cards){
        return cards.get(cards.size()-1);
    }
    public List<Card> giveCards(int noOfCards,List<Card> cards) {
        List<Card> givenCards = new ArrayList<>();
        for (int i = 0; i < noOfCards; i++) {
            Card topCard = cards.get(cards.size()-1);
            cards.remove(cards.size()-1);
            givenCards.add(topCard);
        }

        return givenCards;
    }

    public void decideWhoStarts(){
        int max = 0;
        for(int i = 0 ; i < noOfPlayers ; i++){
            Card topCard = unoDeck.get(unoDeck.size()-1-i);
            if (topCard instanceof NumberedCard) {
                System.out.println(topCard.getValue());
                if (topCard.getValue() > max){
                    max = topCard.getValue();
                    setCurrentPlayerPosition(i);
                }
            }// any other card type (action/wild) is valued 0
        }
        shuffleCards(unoDeck);
        System.out.println(currentPlayerPosition +" starts first ");
    }

    public void shuffleCards(List<Card> unoDeck) {
        Collections.shuffle(unoDeck);
    }

    // Template method for deck creation
    public final void buildUnoDeck(){
        unoDeck.addAll(addNumberedCards());
        unoDeck.addAll(addNormalActionCards());
        unoDeck.addAll(addWildActionCards());
    }

    public void instantiatePlayers(){
        Scanner sc = new Scanner(System.in);
        readNoOfPlayers(sc);
        for (int i = 1 ; i <= noOfPlayers ; i++){
            System.out.println("Enter player "+i+"'s name : ");
            Player player = new Player(sc.next());
            players.add(player);
        }
        System.out.println("Players : ");
        for(int i = 0 ; i < players.size() ; i++)
            System.out.println((i+1)+"- "+players.get(i).getName());
    }

    public int readCardNumber(Scanner sc) {
        System.out.println("Please enter the index of the card you want to play : ");
        int card = 0;
        while(true) {
            card = readIntegerInput(sc);
            if (card >= 1 && card <= players.get(currentPlayerPosition).getNumberOfCards()) {
                return card;
            } else
                System.out.println("Please enter a number in the range (1-7) ");
        }
    }
    public void readNoOfPlayers(Scanner sc) {
        System.out.println("Enter the number of players (2-10) : ");
        int num = 0;
        while(true) {
            num = readIntegerInput(sc);
            if (num >= 2 && num <= 10) {
                setNoOfPlayers(num);
                break;
            } else
                System.out.println("Please enter a number in the range (2-10) ");
        }
    }

    public int readIntegerInput(Scanner sc){
        int input = 0;
        while(true) {
            try {
                input = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format , please enter a number");
            }finally {
                sc.nextLine();
            }
        }
        return input;
    }

    /*---- Standard Uno variation cards creation implementation, can be overridden for customization------*/
    public  List<Card> addNumberedCards(){
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
            NormalActionCards.add(new NormalActionCard(currentColor, Action.REVERSE,20));
            NormalActionCards.add(new NormalActionCard(currentColor,Action.SKIP,20));
            NormalActionCards.add(new NormalActionCard(currentColor,Action.DRAW_2,20));
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

    public List<Player> getPlayers() {
        return players;
    }

    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public void setNoOfPlayers(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
    }

    public int getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }

    public void setCurrentPlayerPosition(int currentPlayerPosition) {
        this.currentPlayerPosition = currentPlayerPosition;
    }

    public GameDirection getGameDirection() {
        return gameDirection;
    }

    public void setGameDirection(GameDirection gameDirection) {
        this.gameDirection = gameDirection;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public List<Card> getUnoDeck() {
        return unoDeck;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public int getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(int pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public void setActionsApplicationStrategy(ActionsApplicationStrategy actionsApplicationStrategy) {
        this.actionsApplicationStrategy = actionsApplicationStrategy;
    }

    public void setPenaltiesApplicationStrategy(PenaltiesApplicationStrategy penaltiesApplicationStrategy) {
        this.penaltiesApplicationStrategy = penaltiesApplicationStrategy;
    }
}
