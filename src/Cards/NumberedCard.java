package Cards;
import Enums.*;
public class NumberedCard extends Card{

    public NumberedCard(Color color, int value) {
        super(color,value);
    }

    @Override
    public boolean canBePlayed(Card topCard) {
        if(this.getColor() == topCard.getColor()) return true;
        if(topCard instanceof NumberedCard)
            return this.getValue() == ((NumberedCard) topCard).getValue();
        return false;
    }

    @Override
    public void print() {
        System.out.println("Numbered Card | "+getColor().toString()+" | "+getValue());
    }
}
