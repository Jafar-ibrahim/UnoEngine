package UnoEngine;

import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.StandardUno;

public class GameDriver {
    public static void main(String[] args) {
        Game StandardUno = new StandardUno();
        StandardUno.play();
    }
}
