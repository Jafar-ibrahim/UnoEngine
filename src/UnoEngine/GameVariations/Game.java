package UnoEngine.GameVariations;

import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;
import UnoEngine.Strategies.ActionStrategies.ActionStrategy;
import UnoEngine.Strategies.CardDealingStrategies.CardDealingStrategy;
import UnoEngine.Strategies.PenaltyStrategies.*;
import UnoEngine.Strategies.StrategyRegistry;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
public abstract class Game {
    String name;
    private List<Card> unoDeck, drawPile, discardPile;
    private int noOfPlayers , pointsToWin;
    private final List<Player> players;
    private int currentPlayerPosition;
    private Color currentColor;
    private GameDirection gameDirection;
    private GameState gameState , roundState;
    private Player gameWinner, roundWinner;
    private StrategyRegistry strategyRegistry;
    private ActionStrategy actionStrategy;
    private PenaltyStrategy penaltyStrategy;
    private CardDealingStrategy cardDealingStrategy;

    public Game(int pointsToWin , GameDirection gameDirection) {
        unoDeck = new ArrayList<>();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
        gameState = GameState.ONGOING;
        this.pointsToWin = pointsToWin;
        this.gameDirection = gameDirection;
        strategyRegistry = new StrategyRegistry();
    }
    public final void play(){
        buildUnoDeck();
        addRequiredStrategies();
        greetPlayers();
        instantiatePlayers();
        shuffleCards(unoDeck);
        decideWhoStarts();
        decideDealingStrategy();
        playRounds();
        announceGameResult();
    }

    protected abstract void addRequiredStrategies();

    /*-----------------------Non-Final Methods--------------------*/
    protected void greetPlayers(){
        System.out.println("Welcome to ("+getName()+") Game !!");
    }
    protected void playRounds() {
        int roundNo = 1;
        while (gameState == GameState.ONGOING){
            cardDealingStrategy.dealCards(this,4);
            System.out.println("*********** Round " + roundNo + " started ******************");

            playOneRound();
            announceRoundResult();
            if (roundState == GameState.A_PLAYER_WON && isGameWinner(roundWinner)) {
                setGameState(GameState.A_PLAYER_WON);
                setGameWinner(roundWinner);
            }
            printPointsTable();
            roundNo++;
            resetCards();
        }
    }
    protected int readCardIndex(Scanner sc) {
        System.out.println("Please enter the index of the card you want to play : \n" +
                "Note: if you have only 1 card left, immediately type \"uno\"(without double quotes) on a new line.");
        int card = readIntegerInput(1,getCurrentPlayer().getNumberOfCards(),sc);
        return card;
    }
    protected void announceRoundResult() {
        if (getRoundState() == GameState.A_PLAYER_WON) {
            System.out.println("--> Round winner is : " + roundWinner.getName());
            roundWinner.addPoints(calcRoundPoints(roundWinner));
            for (Player player : players)
                if (player != roundWinner) player.showCards();

        }else if(getRoundState() == GameState.DRAW) {
            System.out.println("----> Round result is draw !!");
        }
    }
    protected void announceGameResult() {
        if(getGameState() == GameState.A_PLAYER_WON){
            System.out.println("----> Game winner is : " + getGameWinner().getName() +" !!");
        }else if(getGameState() == GameState.DRAW) {
            System.out.println("----> Game Result is draw !!");
        }
    }
    protected boolean playerHasAnyMatchingCards(Player player){
        for (Card card : player.getCards())
            if(cardCanBePlayed(card))
                return true;

        return false;
    }
    protected void checkForUno(){
        if (getCurrentPlayer().getNumberOfCards() == 1){
            if (!saidUno(4000))
                processPenalty(StandardPenalty.FORGOT_UNO,getCurrentPlayer());
        }
    }
    public int getNextPlayerIndex(int InTurnsFromNow){
        int targetPlayer = currentPlayerPosition;
        if(gameDirection == GameDirection.CLOCKWISE)
            while(InTurnsFromNow-- > 0)
                targetPlayer = Math.floorMod(targetPlayer - 1 , noOfPlayers);
        else
            while(InTurnsFromNow-- > 0)
                targetPlayer = Math.floorMod(targetPlayer + 1 ,  noOfPlayers);

        return targetPlayer;
    }
    protected void reshuffleDiscardPile() {
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
    protected void resetCards() {
        for(Player player : getPlayers())
            getUnoDeck().addAll(player.getCards());
        getUnoDeck().addAll(getDiscardPile());
        getUnoDeck().addAll(getDrawPile());
    }
    protected void instantiatePlayers(){
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
    protected boolean saidUno(int timeLimitMS){
        Scanner scanner = new Scanner(System.in);
        // Start the timer for Uno call
        long startTime = System.currentTimeMillis();
        try {
            while ((System.currentTimeMillis() - startTime) < timeLimitMS && System.in.available() == 0) {
                // Wait for input or until the time limit is reached
            }
            // Check if the player called Uno in time
            if (System.in.available() > 0 && ("uno".equalsIgnoreCase(scanner.next())
                                            || scanner.next().contains("uno")
                                            || scanner.next().contains("UNO"))) {
                System.out.println("[Announcement]    Player " + getCurrentPlayer().getName() + " called Uno!");
                return true;
            } else {
                return false;
            }

        }catch (IOException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    protected void readNoOfPlayers(Scanner sc, int maxPlayers){
        System.out.println("Enter the number of players (2-"+maxPlayers+") : ");
        int num = readIntegerInput(2,maxPlayers,sc);
        setNoOfPlayers(num);
    }
    protected void InitializeThePlay() {
        getDrawPile().addAll(getUnoDeck());
        getDiscardPile().clear();
        Card firstCard = drawAndRemoveCard(getDrawPile());

        // First card can't be Wild +4
        while(firstCard instanceof WildActionCard && ((WildActionCard)firstCard).getAction() == WildAction.WILD_DRAW_4){
            getDrawPile().add(firstCard);
            shuffleCards(getDrawPile());
            firstCard = drawAndRemoveCard(getDrawPile());
        }
        getDiscardPile().add(firstCard);
        System.out.print("First card put on discard pile is : ");firstCard.print();
        setCurrentColor(firstCard.getColor());

        if(firstCard instanceof ActionCard)
            processFirstCardInGame(firstCard);

    }
    protected void processFirstCardInGame(Card firstCard){
        // if the first card(drawn from draw pile) is action card , apply it to the first player
        if(firstCard instanceof NormalActionCard){
            // in case of potential penalty :
            // to assign the penalty to the current player, I have to backtrack by one player
            // cause the method flags the next player with a penalty (not the current one)
            setCurrentPlayerPosition(Math.floorMod(getCurrentPlayerPosition() -1 , getNoOfPlayers()));
            processAction(((ActionCard) firstCard).getAction());
            setCurrentPlayerPosition(Math.floorMod(getCurrentPlayerPosition() +1 ,getNoOfPlayers()));
        }else if(firstCard instanceof WildActionCard /*only wild basically*/) {
            getCurrentPlayer().showCards();
            processAction(((ActionCard) firstCard).getAction());
        }
    }
    /*-----------------------Final Methods--------------------*/
    protected final void buildUnoDeck() {
        DeckBuilder deckBuilder = createDeckBuilder();
        List<Card> deck =
                deckBuilder
                        .buildNumberedCards()
                        .buildNormalActionCards()
                        .buildWildActionCards()
                        .getDeck();
        setUnoDeck(deck);
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
    public final void mandatoryDraw(Player currentPlayer , int noCardsToDraw){
        Scanner sc = new Scanner(System.in);
        System.out.println("You dont have any card that can be played , enter 0 to draw a card : ");
        while((sc.nextInt()) != 0){
            System.out.println("Invalid input , please enter 0 to draw a card : ");
        }
        currentPlayer.drawCards(giveCards(noCardsToDraw,drawPile));
        Card drawnCard = peekTopCard(currentPlayer.getCards());
        System.out.print(currentPlayer.getName() + " drew a card : \n"+currentPlayer.getNumberOfCards()+" - ");
        drawnCard.print();
    }
    protected final void decideDealingStrategy() {
        setCardDealingStrategy(createDealingStrategy());
    }
    public final Player getCurrentPlayer(){
        return players.get(currentPlayerPosition);
    }
    public final Player getNextPlayer(int InTurnsFromNow){
        return players.get(getNextPlayerIndex(InTurnsFromNow));
    }
    public final boolean isGameWinner(Player player) {
        return player.getPoints() >= pointsToWin;
    }
    public final void advanceTurn(){
        currentPlayerPosition = getNextPlayerIndex(1);
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
    public final int readIntegerInput(int min , int max,Scanner sc){
        int input ;
        while(true) {
            try {
                input = sc.nextInt();
                if (input >= min && input <= max)
                    break;
                else
                    System.out.println("Please enter a number in the specified range("+min+"-"+max+")");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format , please enter a number :");
            }finally {
                sc.nextLine();
            }
        }
        return input;
    }
    /*-----------------------Abstract Methods--------------------*/
    protected abstract void processAction(Action action);
    protected abstract void processPenalty(Penalty penalty , Player targetPlayer);
    protected abstract DeckBuilder createDeckBuilder() ;
    protected abstract CardDealingStrategy createDealingStrategy();
    protected abstract int calcRoundPoints(Player winner);
    protected abstract boolean cardCanBePlayed(Card card);
    protected abstract void playOneRound();
    protected abstract void decideWhoStarts();
    public List<Player> getPlayers() {
        return players;
    }

    /*-----------------------Setters + Getters--------------------*/
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getNoOfPlayers() {return noOfPlayers;}
    public void setNoOfPlayers(int noOfPlayers) {this.noOfPlayers = noOfPlayers;}
    public int getCurrentPlayerPosition() {return currentPlayerPosition;}
    public void setCurrentPlayerPosition(int currentPlayerPosition) {this.currentPlayerPosition = currentPlayerPosition;}
    public GameDirection getGameDirection() {return gameDirection;}
    public void setGameDirection(GameDirection gameDirection) {this.gameDirection = gameDirection;}
    public GameState getGameState() {return gameState;}
    public void setGameState(GameState gameState) {this.gameState = gameState;}
    public GameState getRoundState() {return roundState;}
    public void setRoundState(GameState roundState) {this.roundState = roundState;}
    public void setUnoDeck(List<Card> unoDeck) {this.unoDeck = unoDeck;}
    public void setDrawPile(List<Card> drawPile) {this.drawPile = drawPile;}
    public void setDiscardPile(List<Card> discardPile) {this.discardPile = discardPile;}
    public List<Card> getUnoDeck() {return unoDeck;}
    public List<Card> getDrawPile() {return drawPile;}
    public List<Card> getDiscardPile() {return discardPile;}
    public int getPointsToWin() {return pointsToWin;}
    public void setPointsToWin(int pointsToWin) {this.pointsToWin = pointsToWin;}
    public void setActionsApplicationStrategy(ActionStrategy actionStrategy) {this.actionStrategy = actionStrategy;}
    public ActionStrategy getActionsApplicationStrategy() {return actionStrategy;}
    public PenaltyStrategy getPenaltiesApplicationStrategy() {return penaltyStrategy;}
    public void setPenaltiesApplicationStrategy(PenaltyStrategy penaltyStrategy) {this.penaltyStrategy = penaltyStrategy;}
    public CardDealingStrategy getCardDealingStrategy() {return cardDealingStrategy;}
    public void setCardDealingStrategy(CardDealingStrategy cardDealingStrategy) {this.cardDealingStrategy = cardDealingStrategy;}
    public Color getCurrentColor() {return currentColor;}
    public void setCurrentColor(Color currentColor) {this.currentColor = currentColor;}
    public StrategyRegistry getStrategyRegistry() {
        return strategyRegistry;
    }
    public Player getGameWinner() {
        return gameWinner;
    }
    public void setGameWinner(Player gameWinner) {
        this.gameWinner = gameWinner;
    }

    public Player getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(Player roundWinner) {
        this.roundWinner = roundWinner;
    }
}
