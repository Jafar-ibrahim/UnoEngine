package Cards;
import Enums.*;
public abstract class Card {

    private Color color;
    private int value;

    public Card(Color color , int value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    //public abstract boolean matches(Card topCard);
    public abstract void print();

}
