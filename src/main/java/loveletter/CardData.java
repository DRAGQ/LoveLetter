package loveletter;
import javax.swing.JLabel;

public class CardData {
	private JLabel card;
    private int index, positionX, positionY, width, height;
    private String cardName;

    public CardData(JLabel card, String cardName, int index, int positionX, int positionY, int width, int height) {
        this.card = card;
        this.cardName = cardName;
        this.index = index;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
    }

    public JLabel getCard() { return card; }
    public String getCardName() { return cardName; }
    public int getIndex() { return index; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
