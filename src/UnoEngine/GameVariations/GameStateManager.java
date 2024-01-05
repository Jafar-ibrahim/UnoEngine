package UnoEngine.GameVariations;

import UnoEngine.Enums.Color;
import UnoEngine.Enums.GameDirection;
import UnoEngine.Enums.GameState;
import UnoEngine.Player;

public class GameStateManager {
    private GameState gameState , roundState;
    private int roundNo;
    private Color currentColor;
    private GameDirection gameDirection;
    Player gameWinner , roundWinner;

    private static GameStateManager instance;

    private GameStateManager(){
        roundState = gameState = GameState.ONGOING;
        gameDirection = GameDirection.CLOCKWISE;
    }
    public static GameStateManager getInstance(){
        if(instance == null)
            instance = new GameStateManager();
        return instance;
    }


    public boolean roundIsWon(){
        return roundState == GameState.A_PLAYER_WON;
    }
    public boolean gameIsWon(){
        return gameState == GameState.A_PLAYER_WON;
    }

    public void updateGameWinner(){
        setGameState(GameState.A_PLAYER_WON);
        setGameWinner(roundWinner);
    }
    public boolean roundIsOngoing(){
        return roundState == GameState.ONGOING;
    }
    public boolean gameIsOngoing(){
        return gameState == GameState.ONGOING;
    }
    public void incrementRounds(){
        roundNo++;
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

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public GameDirection getGameDirection() {
        return gameDirection;
    }

    public void setGameDirection(GameDirection gameDirection) {
        this.gameDirection = gameDirection;
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

    public int getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }
}
