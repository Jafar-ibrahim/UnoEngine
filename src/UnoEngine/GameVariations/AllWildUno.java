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

import java.util.Random;
import java.util.Scanner;

public class AllWildUno extends Game{
    public AllWildUno(int pointsToWin ) {
        this.pointsToWin = pointsToWin;
        setName("All Wild! Uno");
        cardManager = CardManager.getInstance();
        cardManager.setDeckBuilder(new AllWildDeckBuilder());
        playersManager = PlayersManager.getInstance();
        gameStateManager = GameStateManager.getInstance();
        inputOutputManager = InputOutputManager.getInstance();
        turnManager = TurnManager.getInstance();
        strategyRegistry = StrategyRegistry.getInstance();
    }



    @Override
    public void playOneRound() {
        //InitializeThePlay();
        while (getGameStateManager().roundIsOngoing()){
            Player currentPlayer = getTurnManager().getCurrentPlayer();
            checkForPenalty(currentPlayer);

            // if current player changed/skipped due to a penalty then skip this iteration
            if (currentPlayer != getTurnManager().getCurrentPlayer()) continue;

            inputOutputManager.printUserInterface();
            WildActionCard chosenCard = (WildActionCard) inputOutputManager.readCardChoice(currentPlayer);
            currentPlayer.playCard(chosenCard);
            cardManager.discardCard(chosenCard);
            checkForUno(currentPlayer);


            if (chosenCard.getAction() != WildAction.WILD)
                processAction(chosenCard.getAction());

            if (currentPlayer.getNumberOfCards() == 0){
                for(Player player : getPlayersManager().getPlayers()){
                    checkForPenalty(player);
                }
                gameStateManager.setRoundState(GameState.A_PLAYER_WON);
                gameStateManager.setRoundWinner(currentPlayer);
                return;
            }
            turnManager.advanceTurn();
        }
    }

    @Override
    protected void addRequiredStrategies() {
        // For performing card actions that don't apply penalties to players , or
        // applying penalties for the ones who do ( action strategies )
        getStrategyRegistry().addActionStrategy(NormalAction.REVERSE,new ReverseActionStrategy());
        getStrategyRegistry().addActionStrategy(NormalAction.SKIP, new PenaltyAssignmentStrategy(NormalAction.SKIP));
        getStrategyRegistry().addActionStrategy(NormalAction.DRAW_2, new PenaltyAssignmentStrategy(NormalAction.DRAW_2));
        getStrategyRegistry().addActionStrategy(WildAction.WILD_DRAW_4, new CustomWildDraw4Strategy());
        getStrategyRegistry().addActionStrategy(AllWildAction.SKIP_2, new Skip2Strategy());
        getStrategyRegistry().addActionStrategy(AllWildAction.TARGETED_DRAW_2, new TargetedDraw2Strategy());
        getStrategyRegistry().addActionStrategy(AllWildAction.FORCED_SWAP, new ForcedSwapStrategy());

        // For applying the penalties inflicted by action cards ( penalty strategies )
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.SKIP,new SkipPenaltyStrategy());
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.DRAW_2,new Draw2PenaltyStrategy());
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.DRAW_4,new Draw4PenaltyStrategy());
        getStrategyRegistry().addPenaltyStrategy(StandardPenalty.FORGOT_UNO,new ForgotUnoPenaltyStrategy());
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
        for(Player player : getPlayersManager().getPlayers()){
            if(player != winner)
                for(Card card : player.getCards())
                    points += card.getValue();
        }
        System.out.println("Winner got  : "+points+" points !");
        return points;
    }




    /*protected void InitializeThePlay(){
        getDrawPile().addAll(getUnoDeck());
        getDiscardPile().clear();
        Card firstCard = drawAndRemoveCard(getDrawPile());

        getDiscardPile().add(firstCard);
        System.out.print("First card put on discard pile is : ");firstCard.print();
        setCurrentColor(firstCard.getColor());
    }*/
    @Override
    protected void processAction(Action action) {
        ActionStrategy actionStrategy = getStrategyRegistry().getActionStrategy(action);
        if (actionStrategy != null)
            actionStrategy.applyAction(GameContext.getInstance());
    }
    @Override
    protected void processPenalty(Penalty penalty, Player targetPlayer) {
        getTurnManager().getCurrentPlayer().setPenalty(StandardPenalty.NONE);
        PenaltyStrategy penaltyStrategy = getStrategyRegistry().getPenaltyStrategy(penalty);
        penaltyStrategy.applyPenalty(GameContext.getInstance(),targetPlayer);
    }
    /*public void printUserInterface(){
        Card topDiscard = peekTopCard(getDiscardPile());
        System.out.println("------------------ "+getCurrentPlayer().getName()+" turn ------------------");
        System.out.print("Top card on discard pile is : ");topDiscard.print();
        getCurrentPlayer().showCards();
    }*/

    @Override
    public void decideWhoStarts(){
        Random rand = new Random();
        getTurnManager().setCurrentPlayerPosition(rand.nextInt(getPlayersManager().getNoOfPlayers()));
        System.out.println(turnManager.getCurrentPlayer().getName() +" starts first ");
    }
    public void checkForPenalty(Player targetPlayer){
        if(targetPlayer.getPenalty() != StandardPenalty.NONE){
            processPenalty(targetPlayer.getPenalty(), targetPlayer);
        }
    }
}
