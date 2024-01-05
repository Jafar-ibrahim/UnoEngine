package UnoEngine;

import UnoEngine.Cards.Card;
import UnoEngine.Cards.CardManager;
import UnoEngine.GameVariations.GameStateManager;
import UnoEngine.GameVariations.TurnManager;

import java.io.IOException;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputOutputManager {
    private Scanner scanner = new Scanner(System.in);
    private GameStateManager gameStateManager;
    private CardManager cardManager;
    private TurnManager turnManager;
    private PlayersManager playersManager;
    private static InputOutputManager instance;

    private InputOutputManager(){
        playersManager = PlayersManager.getInstance();
        cardManager = CardManager.getInstance();
        turnManager = TurnManager.getInstance();
        gameStateManager = GameStateManager.getInstance();
    }

    public static InputOutputManager getInstance(){
        if (instance == null)
            instance = new InputOutputManager();
        return instance;
    }


    public int readNoOfPlayers(){
        System.out.println("Enter the number of players (2-10) : ");
        return readIntegerInput(2,10);
    }

    public int readColorChoice(){
        System.out.println("Please choose a color to continue(enter the color number):\n" +
                "1-Blue   2-Green   3-Red   4-Yellow");
        int color = readIntegerInput(1,4) - 1;
        return color;
    }
    public void printPlayersNoOfCards(){
        int i = 1;
        for(Player player:playersManager.getPlayers())
            System.out.println(i++ +"- "+player.getName()+" : "+player.getNumberOfCards() +" cards");
    }

    public Player readPlayerChoice(){
        Player chosenPlayer;
        while (true){
            int targetPlayerIndex = readIntegerInput(1, playersManager.getNoOfPlayers()) - 1;
            chosenPlayer = playersManager.getPlayerByIndex(targetPlayerIndex);
            if (chosenPlayer == turnManager.getCurrentPlayer())
                System.out.println("You can't choose yourself , please choose another player : ");
            else
                break;
        }
        return chosenPlayer;
    }

    public final int readIntegerInput(int min , int max){
        int input ;
        while(true) {
            try {
                input = scanner.nextInt();
                if (input >= min && input <= max)
                    break;
                else
                    System.out.println("Please enter a number in the specified range("+min+"-"+max+")");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format , please enter a number :");
            }finally {
                scanner.nextLine();
            }
        }
        return input;
    }

    public final void printPointsTable(List<Player> players) {
        System.out.printf("--------------------------------%n");
        System.out.printf(" Points Table         %n");
        System.out.printf("--------------------------------%n");
        System.out.printf("| %-20s | %-4s |%n", "Name", "Points");
        System.out.printf("--------------------------------%n");
        for(Player player : players.stream().sorted(Comparator.comparingInt(Player::getPoints).reversed()).collect(Collectors.toList()))
            System.out.printf("| %-20s | %-4s   |%n", player.getName(),player.getPoints());

        System.out.printf("--------------------------------%n");
    }
    public boolean timedUnoCall(int timeLimitMS , Player player){
        // Start the timer for Uno call
        long startTime = System.currentTimeMillis();
        try {
            while ((System.currentTimeMillis() - startTime) < timeLimitMS && System.in.available() == 0) {
                // Wait for input or until the time limit is reached
            }
            // Check if the player called Uno in time
            if (System.in.available() > 0 ) {
                String response = scanner.next();
                if(response.equalsIgnoreCase("uno")
                        || response.contains("uno")
                        || response.contains("UNO")){
                    System.out.println("[Announcement]    Player " + player.getName() + " called Uno!");
                    return true;
                }else return false;
            }
            // nothing was typed
            return false;
        }catch (IOException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public void greetPlayers(String gameName){
        System.out.println("Welcome to ("+gameName+") Game !!");
    }

    public void printUserInterface(){
        Card topDiscard = cardManager.peekTopDiscardPile();
        System.out.println("------------------ "+turnManager.getCurrentPlayer().getName()+" turn ------------------");
        System.out.print("Current color : "+gameStateManager.getCurrentColor()+"\nTop card on discard pile is : ");topDiscard.print();
        turnManager.getCurrentPlayer().showCards();
    }
    public Card readCardChoice(Player player) {
        System.out.println("Please enter the index of the card you want to play : \n" +
                "Note: if you have only 1 card left, immediately enter \"uno\"(without double quotes) on a new line.");
        int chosenCardIndex = readIntegerInput(1,player.getNumberOfCards()) - 1;
        Card chosenCard = player.getCards().get(chosenCardIndex);
        while(!cardManager.cardCanBePlayed(chosenCard,player)){
            System.out.println("This card cannot be played , choose another one :");
            chosenCardIndex = readIntegerInput(1,player.getNumberOfCards()) - 1 ;
            chosenCard = player.getCards().get(chosenCardIndex);
        }
        return chosenCard;
    }

    public String[] readPlayersNames(){
        String[] names = new String[playersManager.getNoOfPlayers()];
        for (int i = 1 ; i <= names.length ; i++) {
            System.out.println("Enter player " + i + "'s name : ");
            names[i - 1] = scanner.next();
        }
        return names;
    }



}
