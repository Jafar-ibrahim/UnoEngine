package UnoEngine.GameVariations;
import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;
import UnoEngine.Strategies.ActionStrategies.*;
import UnoEngine.Strategies.CardDealingStrategies.CardDealingStrategy;
import UnoEngine.Strategies.CardDealingStrategies.StandardCardDealingStrategy;
import UnoEngine.Strategies.PenaltyStrategies.*;

import java.util.Scanner;
public class StandardUno extends Game{
    public StandardUno(int pointsToWin , GameDirection gameDirection) {
        super(pointsToWin,gameDirection);
        setName("Standard Uno");
    }

    @Override
    public void playOneRound() {
        InitializeThePlay();
        while (getRoundState() == GameState.ONGOING){
            Player currentPlayer = getCurrentPlayer();
            checkForPenalty(getCurrentPlayer());
            // if current player changed/skipped due to a penalty
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
            setCurrentColor(chosenCard.getColor());
            checkForUno();

            if(chosenCard instanceof ActionCard){
                processAction(((ActionCard)chosenCard).getAction());
            }
            if (currentPlayer.getNumberOfCards() == 0){
                // in case there is someone who needs to draw cards before ending the game
                for(Player player : getPlayers()) checkForPenalty(player);
                setRoundState(GameState.A_PLAYER_WON);
                setRoundWinner(currentPlayer);
                return;
            }
            advanceTurn();
        }
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
        for(Player player : getPlayers()){
            if(player != winner)
                for(Card card : player.getCards())
                    points += card.getValue();
        }
        System.out.println("Winner got  : "+points+" points !");
        return points;
    }
    @Override
    public boolean cardCanBePlayed(Card card){
        Card topDiscard = peekTopCard(getDiscardPile());
        if(card instanceof WildActionCard){
            if (((WildActionCard) card).getAction() == WildAction.WILD_DRAW_4){
                for (Card otherCard : getCurrentPlayer().getCards()) {
                    if (otherCard instanceof WildActionCard)
                        continue;
                    // wild+4 can be only played if no colored card can be played
                    if (cardCanBePlayed(otherCard))
                        return false;
                }
            }
            return true;
        }else{
            if(card.getColor() == getCurrentColor()) return true;
            else if (card instanceof NormalActionCard && topDiscard instanceof NormalActionCard) {
                return ((NormalActionCard) card).getAction() == ((NormalActionCard) topDiscard).getAction();
            }else if(card instanceof NumberedCard){
                return card.getValue() == topDiscard.getValue();
            }
        }
        return false;
    }

    @Override
    protected void processAction(Action action) {
        ActionStrategy actionStrategy =  getStrategyRegistry().getActionStrategy(action);
        if (actionStrategy != null)
            actionStrategy.applyAction(this);
    }
    @Override
    protected void processPenalty(Penalty penalty , Player targetPlayer) {
        getCurrentPlayer().setPenalty(StandardPenalty.NONE);
        PenaltyStrategy penaltyStrategy = getStrategyRegistry().getPenaltyStrategy(penalty);
        penaltyStrategy.applyPenalty(this,targetPlayer);    }

    public void printUserInterface(){
        Card topDiscard = peekTopCard(getDiscardPile());
        System.out.println("------------------ "+getCurrentPlayer().getName()+" turn ------------------");
        System.out.print("Current color : "+getCurrentColor()+"\nTop card on discard pile is : ");topDiscard.print();
        getCurrentPlayer().showCards();
    }
    public void checkForPenalty(Player targetPlayer){
        if(targetPlayer.getPenalty() != StandardPenalty.NONE){
            processPenalty(targetPlayer.getPenalty(),targetPlayer);
        }
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
