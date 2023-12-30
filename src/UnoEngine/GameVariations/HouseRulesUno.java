package UnoEngine.GameVariations;

import UnoEngine.Cards.*;
import UnoEngine.Enums.*;
import UnoEngine.Player;
import UnoEngine.Strategies.ActionStrategies.ChangeColorActionStrategy;
import UnoEngine.Strategies.ActionStrategies.ForcedSwapStrategy;
import UnoEngine.Strategies.ActionStrategies.ReverseActionStrategy;
import UnoEngine.Strategies.CardDealingStrategies.CardDealingStrategy;
import UnoEngine.Strategies.CardDealingStrategies.StandardCardDealingStrategy;
import UnoEngine.Strategies.PenaltyStrategies.*;

import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HouseRulesUno extends Game{
    public HouseRulesUno(int pointsToWin , GameDirection gameDirection) {
        super(pointsToWin, gameDirection);
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
        return true;
    }

    @Override
    public void playOneRound() {
        InitializeThePlay();
        while (true){
            Player currentPlayer = getCurrentPlayer();
            checkForPenalty();
            if (currentPlayer != getCurrentPlayer()) continue;

            printUserInterface();

            int chosenCardIndex = readCardIndex(new Scanner(System.in)) - 1;
            WildActionCard chosenCard = (WildActionCard) currentPlayer.getCards().get(chosenCardIndex);
            currentPlayer.playCard(chosenCard);
            getDiscardPile().add(chosenCard);
            checkForUno();

            if (currentPlayer.getNumberOfCards() == 0){
                setRoundState(GameState.A_PLAYER_WON);
                return;
            }
            if (chosenCard.getAction() != AllWildAction.WILD)
                processAction(chosenCard.getAction());

            advanceTurn();
        }
    }
    @Override
    protected void processAction(Action action) {
        if(action == AllWildAction.WILD_REVERSE){
            setActionsApplicationStrategy(new ReverseActionStrategy());
            getActionsApplicationStrategy().applyAction(this);

        }else if(action == AllWildAction.WILd_FORCED_SWAP){
            setActionsApplicationStrategy(new ForcedSwapStrategy());
        }

        setActionsApplicationStrategy(new PenaltyAssignmentStrategy());
        getActionsApplicationStrategy().applyAction(this);
    }
    @Override
    protected void processPenalty(Penalty penalty) {
        getCurrentPlayer().setPenalty(Penalty.NONE);

        if(penalty == Penalty.DRAW_2){
            setPenaltiesApplicationStrategy(new Draw2PenaltyStrategy());
        }else if(penalty == Penalty.DRAW_4){
            setPenaltiesApplicationStrategy(new Draw4PenaltyStrategy());
        } else if (penalty == Penalty.FORGOT_UNO)
            setPenaltiesApplicationStrategy(new ForgotUnoPenaltyStrategy());
        if (penalty == Penalty.SKIP || penalty == Penalty.DRAW_2 || penalty == Penalty.DRAW_4) {
            setPenaltiesApplicationStrategy(new SkipPenaltyStrategy());
        }

        getPenaltiesApplicationStrategy().applyPenalty(this);
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

    public void checkForPenalty(){
        if(getCurrentPlayer().getPenalty() != Penalty.NONE){
            processPenalty(getCurrentPlayer().getPenalty());
        }
    }
}
