package UnoEngine;

import UnoEngine.Enums.GameDirection;
import UnoEngine.GameVariations.Game;
import UnoEngine.GameVariations.StandardUno;

public class GameDriver {
    public static void main(String[] args) {
        Game StandardUno = new StandardUno(1, GameDirection.CLOCKWISE);
        StandardUno.play();
    }
}
