package UnoEngine.Strategies.ActionStrategies;

import UnoEngine.GameVariations.GameContext;
import UnoEngine.InputOutputManager;
import UnoEngine.Player;
import UnoEngine.Strategies.PenaltyStrategies.SkipPenaltyStrategy;

public class TargetedDraw2Strategy implements ActionStrategy{
    @Override
    public void applyAction(GameContext gameContext) {
        InputOutputManager inputOutputManager = gameContext.getInputOutputManager();
        System.out.println("Forced swap !! These are the numbers(indices) of the players : ");
        System.out.println("Enter the number(index) of the player you want to draw 2 cards : ");

        inputOutputManager.printPlayersNoOfCards();

        Player targetPlayer = inputOutputManager.readPlayerChoice();

        System.out.println("Player "+targetPlayer.getName() +" was chosen.");
        new SkipPenaltyStrategy().applyPenalty(gameContext,targetPlayer);
    }
}
