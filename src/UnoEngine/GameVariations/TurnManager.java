package UnoEngine.GameVariations;

import UnoEngine.Cards.Card;
import UnoEngine.Enums.GameDirection;
import UnoEngine.Player;
import UnoEngine.PlayersManager;

public class TurnManager {

    PlayersManager playersManager;
    GameStateManager gameStateManager;
    private int currentPlayerPosition;
    private Player currentPlayer;
    private static TurnManager instance;

    private TurnManager() {
        playersManager = PlayersManager.getInstance();
        gameStateManager = GameStateManager.getInstance();
        currentPlayerPosition = 1;
        currentPlayer = null;
    }

    public static TurnManager getInstance(){
        if(instance == null)
            instance = new TurnManager();
        return instance;
    }

    public final Player getCurrentPlayer(){
        return playersManager.getPlayerByIndex(currentPlayerPosition);
    }
    public final Player getNextPlayer(int InTurnsFromNow){
        return playersManager.getPlayerByIndex(getNextPlayerIndex(InTurnsFromNow));
    }
    public final void advanceTurn(){
        currentPlayerPosition = getNextPlayerIndex(1);
    }
    public int getNextPlayerIndex(int InTurnsFromNow){
        int targetPlayer = currentPlayerPosition;
        if(gameStateManager.getGameDirection()== GameDirection.CLOCKWISE)
            while(InTurnsFromNow-- > 0)
                targetPlayer = Math.floorMod(targetPlayer - 1 , playersManager.getNoOfPlayers());
        else
            while(InTurnsFromNow-- > 0)
                targetPlayer = Math.floorMod(targetPlayer + 1 ,  playersManager.getNoOfPlayers());

        return targetPlayer;
    }

    public void setCurrentPlayerPosition(int currentPlayerPosition) {
        this.currentPlayerPosition = currentPlayerPosition;
    }
}
