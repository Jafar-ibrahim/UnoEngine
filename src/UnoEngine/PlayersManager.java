package UnoEngine;

import UnoEngine.Cards.Card;
import UnoEngine.Enums.GameDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayersManager {
    private List<Player> players;
    private int noOfPlayers;

    private static PlayersManager instance;

    private PlayersManager() {
        players = new ArrayList<>();
    }

    public static PlayersManager getInstance(){
        if(instance == null)
            instance = new PlayersManager();
        return instance;
    }
    public void instantiatePlayers(String[] names){
        System.out.println(noOfPlayers);
        for (int i = 1 ; i <= noOfPlayers ; i++){
            Player player = new Player(names[i-1]);
            players.add(player);
        }
    }
    public Player getPlayerByIndex(int index){
        if (index < 0 || index > noOfPlayers - 1)
            throw new IllegalArgumentException();

        return players.get(index);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public void setNoOfPlayers(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
    }
}
