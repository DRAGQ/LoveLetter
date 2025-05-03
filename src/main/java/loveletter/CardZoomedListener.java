package loveletter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class CardZoomedListener extends MouseAdapter {

	public static boolean isCardOpened;
	private String cardName;
	private ShadowPane shadowPane;
	private JLabel zoomedCardImage, exitImage;
	private JLayeredPane layeredPane;
	private EscMenuGui escMenu;
	
	public CardZoomedListener(String cardName, JLayeredPane layeredPane, ShadowPane shadowPane, EscMenuGui escMenu) {
		this.cardName = cardName;
		this.shadowPane = shadowPane;
		this.zoomedCardImage = new JLabel();
		this.exitImage = new JLabel();
		this.layeredPane = layeredPane;
		this.escMenu = escMenu;
		CardZoomedListener.isCardOpened = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (CardZoomedListener.isCardOpened) {
			return;
		}
		CardZoomedListener.isCardOpened = true;
		ImageIcon bigCardIcon = new ImageIcon(getClass().getResource("/bigCards/" + this.cardName + ".png"));
		ImageIcon exitIcon = new ImageIcon(getClass().getResource("/bigCards/exitButton.png"));
		ImageIcon exitIcon2 = new ImageIcon(getClass().getResource("/bigCards/exitButton2.png"));
		int layWidth = this.layeredPane.getWidth();
		int layHeight = this.layeredPane.getHeight();
		int imageWidth = bigCardIcon.getIconWidth();
		int imageHeight = bigCardIcon.getIconHeight();
		int x  = layWidth / 2 - imageWidth / 2;
		int y = layHeight / 2 - imageHeight / 2;
		int exitX = x + imageWidth - exitIcon.getIconWidth();

		this.zoomedCardImage.setIcon(bigCardIcon);
		this.exitImage.setIcon(exitIcon);
		this.zoomedCardImage.setBounds(x,y, imageWidth, imageHeight);
		this.exitImage.setBounds(exitX, y, exitIcon.getIconWidth(), exitIcon.getIconHeight());
		this.exitImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitImage.setIcon(exitIcon2);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitImage.setIcon(exitIcon);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				layeredPane.remove(exitImage);
				layeredPane.remove(zoomedCardImage);
				CardZoomedListener.this.shadowPane.decreaseCount();
				layeredPane.revalidate();
				layeredPane.repaint();
				isCardOpened = false;
			}
		});
		
		escMenu.zoomedCardIsOpened(this.zoomedCardImage, this.exitImage);
		this.shadowPane.addCount();
		this.layeredPane.add(this.zoomedCardImage, JLayeredPane.POPUP_LAYER);
		this.layeredPane.add(this.exitImage, JLayeredPane.DRAG_LAYER);
		
		this.layeredPane.revalidate();
		this.layeredPane.repaint();
	}
	
}
