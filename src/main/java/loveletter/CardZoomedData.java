package loveletter;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardZoomedData {
	
	String cardName;
	JLabel zoomedCardImage, exitImage;
	JPanel backgroundPanel;
	
	public CardZoomedData(String cardName, JLabel zoomedCardImage, JLabel exitImage, JPanel backgroundPanel) {
		this.cardName = cardName;
		this.zoomedCardImage = zoomedCardImage;
		this.exitImage = exitImage;
		this.backgroundPanel = backgroundPanel;
	}
	
	public String getCardName() { return cardName; }
	public JLabel getZoomedCardImage() { return zoomedCardImage; }
	public JLabel getExitImage() { return exitImage; }
	public JPanel getBackgroundPanel() { return backgroundPanel; }
}
