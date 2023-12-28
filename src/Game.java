import Cards.*;
import Enums.*;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Game {
    private List<Card> unoDeck, drawPile, discardPile;
    private int noOfPlayers , pointsToWin;
    private final List<Player> players;
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
        gameState = GameState.ONGOING;
    }

    public final void play(){
        buildUnoDeck();
        instantiatePlayers();
        shuffleCards(unoDeck);
        decidePointsToWin();
        decideWhoStarts();
        decideInitialGameDirection();
        playRounds();
        announceResult();
    }

    public void announceResult() {
        if(getGameState() == GameState.A_PLAYER_WON){
            System.out.println("----> Game winner is : " + getCurrentPlayer().getName() +" !!");
        }else if(getGameState() == GameState.DRAW) {
            System.out.println("----> Result is draw !!");
        }
    }

    public void playRounds() {
        int roundNo = 1;
        while (gameState == GameState.ONGOING){
            dealCards(2);
            System.out.println("*********** Round " + roundNo + " started ******************");

            int roundWinnerIndex = playOneRound();
            Player roundWinner =  players.get(roundWinnerIndex);
            System.out.println("--> Round "+roundNo+" winner is : "+roundWinner.getName());
            roundWinner.addPoints(calcRoundPoints(roundWinner));
            for (Player player : players)
                if (player != roundWinner) player.showCards();
            printPointsTable();

            if(isGameWinner(roundWinner)){
                setGameState(GameState.A_PLAYER_WON);
            }
            roundNo++;
            resetCards();
        }
    }

    public void printPointsTable() {
        System.out.printf("--------------------------------%n");
        System.out.printf(" Points Table         %n");
        System.out.printf("--------------------------------%n");
        System.out.printf("| %-20s | %-4s |%n", "Name", "Points");
        System.out.printf("--------------------------------%n");
        for(Player player : players.stream().sorted(Comparator.comparingInt(Player::getPoints).reversed()).collect(Collectors.toList()))
            System.out.printf("| %-20s | %-4s   |%n", player.getName(),player.getPoints());

        System.out.printf("--------------------------------%n");
    }

    public int calcRoundPoints(Player winner) {
        int points = 0;
        for(Player player : players){
            if(player != winner)
                for(Card card : player.getCards())
                    points += card.getValue();
        }
        System.out.println("Winner got  : "+points+" points !");
        return points;
    }

    public void resetCards() {
        for(Player player : players)
           unoDeck.addAll(player.getCards());
        unoDeck.addAll(discardPile);
        unoDeck.addAll(drawPile);
    }

    public boolean cardCanBePlayed(Card card){
        Card topDiscard = getTopCard(discardPile);
        if(card instanceof WildActionCard){
            if (((WildActionCard) card).getAction() == WildAction.CHANGE_COLOR_AND_DRAW_4){
                for (Card otherCard : getCurrentPlayer().getCards())
                    if (otherCard != card && cardCanBePlayed(otherCard)) return false;
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
    public boolean playerHasAnyMatchingCards(Player player){
        for (Card card : player.getCards())
            if(cardCanBePlayed(card))
                return true;

        return false;
    }

    public int playOneRound() {
        InitializeThePlay();
        while (true){
            Player currentPlayer = getCurrentPlayer();
            // checking for any penalties that have to be applied
            if(currentPlayer.penalty != Penalty.NONE){
                processPenalty(currentPlayer.penalty);
            }
            if (currentPlayer != getCurrentPlayer()) continue;

            Card topDiscard = getTopCard(discardPile);
            System.out.println("------------------ "+currentPlayer.getName()+" turn ------------------");
            System.out.print("Top card on discard pile is : ");topDiscard.print();
            // reading the player's card choice
            currentPlayer.showCards();
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
            discardPile.add(chosenCard);

            if (currentPlayer.getNumberOfCards() == 1){
                if (!saidUno(4000))
                    processPenalty(Penalty.FORGOT_UNO);
            }
            if (currentPlayer.getNumberOfCards() == 0){
                return currentPlayerPosition;
            }
            if(chosenCard instanceof ActionCard){
                processAction(((ActionCard)chosenCard).getAction());
            }

            advanceTurn();
        }
    }

    public boolean emergencyDraw(Player currentPlayer , int noCardsToDraw){
        Scanner sc = new Scanner(System.in);
        System.out.println("You dont have any card that can be played , enter 0 to draw a card : ");
        while((sc.nextInt()) != 0){
            System.out.println("Invalid input , please enter 0 to draw a card : ");
        }
        currentPlayer.drawCards(giveCards(noCardsToDraw,drawPile));
        Card drawnCard = getTopCard(currentPlayer.getCards());
        System.out.print(currentPlayer.getName() + " drew a card : \n"+currentPlayer.getNumberOfCards()+" -");
        drawnCard.print();

        return cardCanBePlayed(drawnCard);
    }
    public void showChoicesMenu(Player currentPlayer , Card topDiscard){

    }


    public void InitializeThePlay() {

        drawPile.addAll(unoDeck);
        unoDeck.clear();
        Card firstCard = getTopCard(drawPile);

        // First card can't be Wild +4
        while(firstCard instanceof WildActionCard && ((WildActionCard)firstCard).getAction() == WildAction.CHANGE_COLOR_AND_DRAW_4 ){
            drawPile.add(firstCard);
            Collections.shuffle(drawPile);
            firstCard = getTopCard(drawPile);
        }
        discardPile.add(firstCard);
        System.out.print("First card put on discard pile is : ");firstCard.print();

        // if first card is action card , apply it to the first player
        if(firstCard instanceof NormalActionCard){
            // in case of potential penalty :
            // to assign the penalty to the current player, I have to backtrack by one player
            // cause the method flags the next player with a penalty (not the current one)
            currentPlayerPosition = Math.floorMod(currentPlayerPosition -1 , noOfPlayers);
            processAction(((ActionCard) firstCard).getAction());
            currentPlayerPosition = Math.floorMod(currentPlayerPosition +1 ,noOfPlayers);
        }else if(firstCard instanceof WildActionCard /*only wild basically*/) {
            processAction(((ActionCard) firstCard).getAction());
        }


    }

    public void processAction(Action action) {
        if(action == NormalAction.REVERSE){
            setActionsApplicationStrategy(new ReverseStrategy());
        }else if(action == WildAction.CHANGE_COLOR || action == WildAction.CHANGE_COLOR_AND_DRAW_4){
            setActionsApplicationStrategy(new ChangeColorStrategy());
            if(action == WildAction.CHANGE_COLOR_AND_DRAW_4){
                actionsApplicationStrategy.applyAction(this);
                setActionsApplicationStrategy(new PenaltyAssignmentStrategy());
            }
        }else
            setActionsApplicationStrategy(new PenaltyAssignmentStrategy());

        actionsApplicationStrategy.applyAction(this);
    }

    public void processPenalty(Penalty penalty) {
        getCurrentPlayer().setPenalty(Penalty.NONE);

        if(penalty == Penalty.DRAW_2){
            setPenaltiesApplicationStrategy(new Draw2Strategy());
        }else if(penalty == Penalty.DRAW_4){
            setPenaltiesApplicationStrategy(new Draw4Strategy());
        } else if (penalty == Penalty.SKIP) {
            setPenaltiesApplicationStrategy(new SkipStrategy());
        }else if (penalty == Penalty.FORGOT_UNO)
            setPenaltiesApplicationStrategy(new ForgotUnoStrategy());

        penaltiesApplicationStrategy.applyPenalty(this);
    }
    public Player getCurrentPlayer(){
        return players.get(currentPlayerPosition);
    }

    public boolean isGameWinner(Player player) {
        return player.getPoints() >= pointsToWin;
    }
    public boolean RoundIsOver() {
        for (Player player : players){
            if(player.getCards().isEmpty())
                return true;
        }
        return false;
    }
    public void decidePointsToWin() {
        setPointsToWin(1);
    }

    public void decideInitialGameDirection() {
        setGameDirection(GameDirection.CLOCKWISE);
    }

    public void dealCards(int noOfCardsEach) {
        for(int i = 0 ; i < noOfCardsEach ; i++){
            for(int j = 0 ; j < noOfPlayers ; j++){
                getCurrentPlayer().drawCards(giveCards(1,unoDeck));
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
                if (topCard.getValue() > max){
                    max = topCard.getValue();
                    setCurrentPlayerPosition(i);
                }
            }// any other card type (action/wild) is valued 0
        }
        shuffleCards(unoDeck);
        System.out.println(players.get(currentPlayerPosition).getName() +" starts first ");
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


    public boolean saidUno(int timeLimitMS){
        Scanner scanner = new Scanner(System.in);
        // Start the timer for Uno call
        long startTime = System.currentTimeMillis();
        try {
            while ((System.currentTimeMillis() - startTime) < timeLimitMS && System.in.available() == 0) {
                // Wait for input or until the time limit is reached
            }
        // Check if the player called Uno in time
        if (System.in.available() > 0 && "uno".equalsIgnoreCase(scanner.next())) {
            System.out.println("Player " + getCurrentPlayer().getName() + " called Uno!");
            return true;
        } else {
            System.out.println("Player " + getCurrentPlayer().getName() + " didn't call Uno in time. Penalty!");
            return false;
        }
        }catch (IOException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public int readCardIndex(Scanner sc) {
        System.out.println("Please enter the index of the card you want to play : \n" +
                "Note: if you have only 1 card left, immediately type \"uno\"(without double quotes) on a new line");
        int card = 0;
        while(true) {
            card = readIntegerInput(sc);
            Player currentPlayer = players.get(currentPlayerPosition);
            if (card >= 1 && card <= currentPlayer.getNumberOfCards()) {
                return card;
            } else
                System.out.println("Please enter a number in the range (1"+"-"+currentPlayer.getNumberOfCards()+") ");
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
