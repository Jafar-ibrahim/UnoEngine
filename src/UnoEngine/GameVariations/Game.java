package UnoEngine.GameVariations;

import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;
import UnoEngine.Strategies.ActionStrategies.ActionsApplicationStrategy;
import UnoEngine.Strategies.ActionStrategies.ChangeColorActionStrategy;
import UnoEngine.Strategies.ActionStrategies.ReverseActionStrategy;
import UnoEngine.Strategies.PenaltyStrategies.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
public abstract class Game {
    private List<Card> unoDeck, drawPile, discardPile;
    private int noOfPlayers , pointsToWin;
    private final List<Player> players;
    private int currentPlayerPosition;
    private GameDirection gameDirection;
    private GameState gameState , roundState;
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
        announceGameResult();
    }
    public void playRounds() {
        int roundNo = 1;
        while (gameState == GameState.ONGOING){
            dealCards(2);
            System.out.println("*********** Round " + roundNo + " started ******************");

            playOneRound();
            announceRoundResult();
            if (isGameWinner(getCurrentPlayer())) {
                setGameState(GameState.A_PLAYER_WON);
            }
            printPointsTable();
            roundNo++;
            resetCards();
        }
    }
    public int readCardIndex(Scanner sc) {
        System.out.println("Please enter the index of the card you want to play : \n" +
                "Note: if you have only 1 card left, immediately type \"uno\"(without double quotes) on a new line.");
        int card = 0;
        while(true) {
            card = readIntegerInput(sc);
            Player currentPlayer = getPlayers().get(getCurrentPlayerPosition());
            if (card >= 1 && card <= currentPlayer.getNumberOfCards()) {
                return card;
            } else
                System.out.println("Please enter a number in the range (1"+"-"+currentPlayer.getNumberOfCards()+") ");
        }
    }
    public void announceRoundResult() {
        if (getRoundState() == GameState.A_PLAYER_WON) {
            Player roundWinner = players.get(currentPlayerPosition);
            System.out.println("--> Round winner is : " + roundWinner.getName());
            roundWinner.addPoints(calcRoundPoints(roundWinner));
            for (Player player : players)
                if (player != roundWinner) player.showCards();

        }else if(getRoundState() == GameState.DRAW) {
            System.out.println("----> Round result is draw !!");
        }
    }
    public void announceGameResult() {
        if(getGameState() == GameState.A_PLAYER_WON){
            System.out.println("----> Game winner is : " + getCurrentPlayer().getName() +" !!");
        }else if(getGameState() == GameState.DRAW) {
            System.out.println("----> Game Result is draw !!");
        }
    }
    public boolean playerHasAnyMatchingCards(Player player){
        for (Card card : player.getCards())
            if(cardCanBePlayed(card))
                return true;

        return false;
    }

    public void processAction(Action action) {
        if(action == NormalAction.REVERSE){
            setActionsApplicationStrategy(new ReverseActionStrategy());
        }else if(action == WildAction.CHANGE_COLOR || action == WildAction.CHANGE_COLOR_AND_DRAW_4){
            setActionsApplicationStrategy(new ChangeColorActionStrategy());
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
            setPenaltiesApplicationStrategy(new Draw2PenaltyStrategy());
        }else if(penalty == Penalty.DRAW_4){
            setPenaltiesApplicationStrategy(new Draw4PenaltyStrategy());
        } else if (penalty == Penalty.SKIP) {
            setPenaltiesApplicationStrategy(new SkipPenaltyStrategy());
        }else if (penalty == Penalty.FORGOT_UNO)
            setPenaltiesApplicationStrategy(new ForgotUnoPenaltyStrategy());

        penaltiesApplicationStrategy.applyPenalty(this);
    }
    public int getNextPlayerIndex(){
        if(gameDirection == GameDirection.CLOCKWISE)
            return Math.floorMod(currentPlayerPosition - 1 , noOfPlayers);
        else
            return Math.floorMod(currentPlayerPosition + 1 ,  noOfPlayers);
    }
    public void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            // Not enough cards to reshuffle;declare a draw.
            setRoundState(GameState.DRAW);
            return;
        }
        // Keep the top card
        Card topCard = discardPile.remove(0);
        shuffleCards(discardPile);

        drawPile.addAll(discardPile);
        discardPile.clear();
        discardPile.add(topCard);
    }
    public void buildUnoDeck(){
        unoDeck.addAll(addNumberedCards());
        unoDeck.addAll(addNormalActionCards());
        unoDeck.addAll(addWildActionCards());
    }
    public void instantiatePlayers(){
        Scanner sc = new Scanner(System.in);
        readNoOfPlayers(sc,10);
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

    public void readNoOfPlayers(Scanner sc, int maxPlayers){
        System.out.println("Enter the number of players (2-"+maxPlayers+") : ");
        int num = 0;
        while(true) {
            num = readIntegerInput(sc);
            if (num >= 2 && num <= 10) {
                setNoOfPlayers(num);
                break;
            } else
                System.out.println("Please enter a number in the range (2-"+maxPlayers+") ");
        }
    }

    public final void printPointsTable() {
        System.out.printf("--------------------------------%n");
        System.out.printf(" Points Table         %n");
        System.out.printf("--------------------------------%n");
        System.out.printf("| %-20s | %-4s |%n", "Name", "Points");
        System.out.printf("--------------------------------%n");
        for(Player player : players.stream().sorted(Comparator.comparingInt(Player::getPoints).reversed()).collect(Collectors.toList()))
            System.out.printf("| %-20s | %-4s   |%n", player.getName(),player.getPoints());

        System.out.printf("--------------------------------%n");
    }
    public final boolean emergencyDraw(Player currentPlayer , int noCardsToDraw){
        Scanner sc = new Scanner(System.in);
        System.out.println("You dont have any card that can be played , enter 0 to draw a card : ");
        while((sc.nextInt()) != 0){
            System.out.println("Invalid input , please enter 0 to draw a card : ");
        }
        currentPlayer.drawCards(giveCards(noCardsToDraw,drawPile));
        Card drawnCard = peekTopCard(currentPlayer.getCards());
        System.out.print(currentPlayer.getName() + " drew a card : \n"+currentPlayer.getNumberOfCards()+" -");
        drawnCard.print();

        return cardCanBePlayed(drawnCard);
    }
    public final Player getCurrentPlayer(){
        return players.get(currentPlayerPosition);
    }
    public final boolean isGameWinner(Player player) {
        return player.getPoints() >= pointsToWin;
    }
    public final void advanceTurn(){
        currentPlayerPosition = getNextPlayerIndex();
    }
    public final Card peekTopCard(List<Card> cards){
        return cards.get(cards.size()-1);
    }
    public final Card drawAndRemoveCard(List<Card> cards){
        if(cards.isEmpty()) reshuffleDiscardPile();

        Card topCard = cards.get(cards.size()-1);
        cards.remove(cards.size()-1);
        return topCard;
    }
    public final List<Card> giveCards(int noOfCards,List<Card> cards) {
        List<Card> givenCards = new ArrayList<>();
        for (int i = 0; i < noOfCards; i++) {
            givenCards.add(drawAndRemoveCard(cards));
        }
        return givenCards;
    }
    public final void shuffleCards(List<Card> unoDeck) {
        Collections.shuffle(unoDeck);
    }
    public final int readIntegerInput(Scanner sc){
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



    public abstract int calcRoundPoints(Player winner);
    public abstract void resetCards();
    public abstract boolean cardCanBePlayed(Card card);
    public abstract void playOneRound();
    public abstract void InitializeThePlay();
    public abstract void decidePointsToWin();
    public abstract void decideInitialGameDirection();
    public abstract void dealCards(int noOfCardsEach);
    public abstract void decideWhoStarts();
    public abstract List<Card> addNumberedCards();
    public abstract List<Card> addNormalActionCards();
    public abstract List<Card> addWildActionCards();
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

    public GameState getRoundState() {
        return roundState;
    }

    public void setRoundState(GameState roundState) {
        this.roundState = roundState;
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
