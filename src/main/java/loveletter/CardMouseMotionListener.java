package loveletter;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CardMouseMotionListener extends MouseAdapter {
	
	JLabel card;
	JPanel activePlayerCardsPanel;
	JLayeredPane layeredPane;
	
	public CardMouseMotionListener(JLabel card, JPanel activePlayerCardsPanel, JLayeredPane layeredPane) {
		this.card = card;
		this.activePlayerCardsPanel = activePlayerCardsPanel;
		this.layeredPane = layeredPane;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point screenPoint = new Point(e.getXOnScreen(), e.getYOnScreen());
		SwingUtilities.convertPointFromScreen(screenPoint, layeredPane);
		int x = screenPoint.x - card.getWidth() / 2;
		int y = screenPoint.y - card.getHeight() / 2;
		card.setLocation(x, y);
        
        activePlayerCardsPanel.remove(card);
        layeredPane.remove(card);
		layeredPane.add(card, JLayeredPane.DRAG_LAYER);
		layeredPane.moveToFront(card);
		layeredPane.revalidate();
		layeredPane.repaint();
    }
	
	public void removeMouseMotionListener() {
		if (card != null) {
			card.removeMouseMotionListener(this);
		}
	}
}
