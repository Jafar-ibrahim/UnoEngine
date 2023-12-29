package UnoEngine.Cards;
import UnoEngine.Enums.*;
public class NumberedCard extends Card{

    public NumberedCard(Color color, int value) {
        super(color,value);
    }
    @Override
    public void print() {
        System.out.println("Numbered Card | "+getColor().toString()+" | "+getValue());
    }
}
