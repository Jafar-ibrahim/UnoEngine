package UnoEngine.GameVariations;

import UnoEngine.Cards.CardManager;
import UnoEngine.InputOutputManager;
import UnoEngine.PlayersManager;

public class GameContext {
    PlayersManager playersManager;
    CardManager cardManager;
    TurnManager turnManager;
    GameStateManager gameStateManager;
    InputOutputManager inputOutputManager;

    private static GameContext instance;

    private GameContext() {
        playersManager = PlayersManager.getInstance();
        cardManager = CardManager.getInstance();
        turnManager = TurnManager.getInstance();
        gameStateManager = GameStateManager.getInstance();
        inputOutputManager = InputOutputManager.getInstance();
    }
    public static GameContext getInstance(){
        if (instance == null)
            instance = new GameContext();
        return instance;
    }

    public PlayersManager getPlayersManager() {
        return playersManager;
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public InputOutputManager getInputOutputManager() {
        return inputOutputManager;
    }
}
