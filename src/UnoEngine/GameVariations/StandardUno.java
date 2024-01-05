package UnoEngine.GameVariations;
import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.InputOutputManager;
import UnoEngine.Player;
import UnoEngine.PlayersManager;
import UnoEngine.Strategies.ActionStrategies.*;
import UnoEngine.Strategies.CardDealingStrategies.CardDealingStrategy;
import UnoEngine.Strategies.CardDealingStrategies.StandardCardDealingStrategy;
import UnoEngine.Strategies.PenaltyStrategies.*;
import UnoEngine.Strategies.StrategyRegistry;

import java.util.Scanner;
public class StandardUno extends Game{
    public StandardUno(int pointsToWin ) {
        this.pointsToWin = pointsToWin;
        setName("Standard Uno");
        cardManager = CardManager.getInstance();
        playersManager = PlayersManager.getInstance();
        gameStateManager = GameStateManager.getInstance();
        inputOutputManager = InputOutputManager.getInstance();
        turnManager = TurnManager.getInstance();
        strategyRegistry = StrategyRegistry.getInstance();
    }

    @Override
    public void playOneRound() {
        InitializeThePlay();
        while (getGameStateManager().roundIsOngoing()){
            Player currentPlayer = getTurnManager().getCurrentPlayer();
            checkForPenalty(currentPlayer);

            // if current player changed/skipped due to a penalty then skip this iteration
            if (currentPlayer != getTurnManager().getCurrentPlayer()) continue;

            getInputOutputManager().printUserInterface();

            if(!playerHasAnyMatchingCards(currentPlayer)){
                Card drawnCard= mandatoryDraw(currentPlayer,1);
                if (!cardManager.cardCanBePlayed(drawnCard,currentPlayer)) {
                    turnManager.advanceTurn();
                    continue;
                }
            }

            Card chosenCard = inputOutputManager.readCardChoice(currentPlayer);

            currentPlayer.playCard(chosenCard);

            getCardManager().discardCard(chosenCard);
            gameStateManager.setCurrentColor(chosenCard.getColor());

            checkForUno(currentPlayer);

            if(chosenCard instanceof ActionCard){
                processAction(((ActionCard)chosenCard).getAction());
            }
            if (currentPlayer.getNumberOfCards() == 0){
                // in case there is someone who needs to draw cards before
                // ending the game (matters in points calculation)
                for(Player player : getPlayersManager().getPlayers()) checkForPenalty(player);
                gameStateManager.setRoundState(GameState.A_PLAYER_WON);
                gameStateManager.setRoundWinner(currentPlayer);
                return;
            }
            turnManager.advanceTurn();
        }
    }

    public void InitializeThePlay(){
        Card firstCard = getCardManager().peekTopDiscardPile();
        if(firstCard instanceof ActionCard)
            processFirstCardInGame(firstCard);
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
    protected void addRequiredStrategies() {
        // For performing card actions that don't apply penalties to players , or
        // applying penalties for the ones who do ( action strategies )
        getStrategyRegistry().addActionStrategy(NormalAction.REVERSE,new ReverseActionStrategy());
        getStrategyRegistry().addActionStrategy(WildAction.WILD,new ChangeColorActionStrategy());
        getStrategyRegistry().addActionStrategy(NormalAction.SKIP, new PenaltyAssignmentStrategy(NormalAction.SKIP));
        getStrategyRegistry().addActionStrategy(NormalAction.DRAW_2, new PenaltyAssignmentStrategy(NormalAction.DRAW_2));
        getStrategyRegistry().addActionStrategy(WildAction.WILD_DRAW_4, new WildDraw4ActionStrategy());

        // For applying the penalties inflicted by action cards ( penalty strategies )
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.SKIP,new SkipPenaltyStrategy());
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.DRAW_2,new Draw2PenaltyStrategy());
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.DRAW_4,new Draw4PenaltyStrategy());
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.FORGOT_UNO,new ForgotUnoPenaltyStrategy());
    }
    @Override
    public int calcRoundPoints(Player winner) {
        int points = 0;
        for(Player player : playersManager.getPlayers()){
            if(player != winner)
                for(Card card : player.getCards())
                    points += card.getValue();
        }
        System.out.println("Winner got  : "+points+" points !");
        return points;
    }


    @Override
    protected void processAction(Action action) {
        ActionStrategy actionStrategy =  getStrategyRegistry().getActionStrategy(action);
        if (actionStrategy != null)
            actionStrategy.applyAction(GameContext.getInstance());
    }
    @Override
    protected void processPenalty(Penalty penalty , Player targetPlayer) {
        targetPlayer.setPenalty(StandardPenalty.NONE);
        PenaltyStrategy penaltyStrategy = getStrategyRegistry().getPenaltyStrategy(penalty);
        penaltyStrategy.applyPenalty(GameContext.getInstance(),targetPlayer);
    }


    public void checkForPenalty(Player targetPlayer){
        if(targetPlayer.getPenalty() != StandardPenalty.NONE){
            processPenalty(targetPlayer.getPenalty(),targetPlayer);
        }
    }

    @Override
    public void decideWhoStarts(){
        // every player picks a card and the player who got the greater numbered card starts first
        int max = 0;
        for(int i = 0 ; i < playersManager.getNoOfPlayers() ; i++){
            Card topCard = cardManager.drawAndRemoveCard(cardManager.getUnoDeck());
            //Card topCard = getUnoDeck().get(getUnoDeck().size()-1-i);
            if (topCard instanceof NumberedCard) {
                if (topCard.getValue() > max){
                    max = topCard.getValue();
                    turnManager.setCurrentPlayerPosition(i);
                }
            }// any other card type (action/wild) is valued 0
        }
        cardManager.shuffleDeck();
        System.out.println(turnManager.getCurrentPlayer().getName() +" starts first ");
    }
}
