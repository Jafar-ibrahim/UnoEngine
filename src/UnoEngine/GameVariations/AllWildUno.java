package UnoEngine.GameVariations;

import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;
import UnoEngine.Strategies.ActionStrategies.*;
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
        // all cards are playable at any time in this uno variation(All Wild! )
        return true;
    }

    @Override
    public void playOneRound() {
        System.out.println();
        InitializeThePlay();
        while (true){
            Player currentPlayer = getCurrentPlayer();
            checkForPenalty(getCurrentPlayer());
            if (currentPlayer != getCurrentPlayer()) continue;

            printUserInterface();

            int chosenCardIndex = readCardIndex(new Scanner(System.in)) - 1;
            WildActionCard chosenCard = (WildActionCard) currentPlayer.getCards().get(chosenCardIndex);
            currentPlayer.playCard(chosenCard);
            getDiscardPile().add(chosenCard);
            checkForUno();

            if (chosenCard.getAction() != WildAction.WILD)
                processAction(chosenCard.getAction());

            if (currentPlayer.getNumberOfCards() == 0){
                for(Player player : getPlayers()){
                    checkForPenalty(player);
                }
                setRoundState(GameState.A_PLAYER_WON);
                setRoundWinner(currentPlayer);
                return;
            }

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
        ActionStrategy actionStrategy =   getStrategyRegistry().getActionStrategy(action);
        if (actionStrategy != null)
            actionStrategy.applyAction(this);
    }
    @Override
    protected void processPenalty(Penalty penalty, Player targetPlayer) {
        getCurrentPlayer().setPenalty(StandardPenalty.NONE);
        PenaltyStrategy penaltyStrategy = getStrategyRegistry().getPenaltyStrategy(penalty);
        penaltyStrategy.applyPenalty(this,targetPlayer);
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
    public void checkForPenalty(Player targetPlayer){
        if(targetPlayer.getPenalty() != StandardPenalty.NONE){
            processPenalty(targetPlayer.getPenalty(), targetPlayer);
        }
    }
}
