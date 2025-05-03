package loveletter;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class CardMouseListener extends MouseAdapter {
	
	private CardData cardData;
	private CardListenerContext cardContext;
	private GameController controller;
	private static Boolean isCardUsed;
	private int currentPlayerIndex;
	private List<Integer> availablePlayersIndexes;
	private CardMouseMotionListener otherCardMouseMotionListener;
	
	public CardMouseListener(CardData cardData, CardListenerContext cardContext, GameController controller, int currentPlayerIndex, List<Integer> availablePlayersIndexes, CardMouseMotionListener otherCardMouseMotionListener) {
		this.cardData = cardData;
		this.cardContext = cardContext;
		this.controller = controller;
		this.currentPlayerIndex = currentPlayerIndex;
		this.availablePlayersIndexes = availablePlayersIndexes;
		this.otherCardMouseMotionListener = otherCardMouseMotionListener;
		CardMouseListener.isCardUsed = false;
	}
	
		@Override
		public void mouseEntered(MouseEvent e) {
			if (!CardMouseListener.isCardUsed) {
				for (int activePlayerIndex : availablePlayersIndexes) {
					cardContext.getPlayersPanel().get(activePlayerIndex).markPlayedCardsPanel();
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			for (int activePlayerIndex : availablePlayersIndexes) {
				cardContext.getPlayersPanel().get(activePlayerIndex).unmarkPlayedCardsPanel();
			}

		}
		
		@Override
    	public void mouseReleased(MouseEvent e) {
			
			for (int activePlayerIndex : availablePlayersIndexes) {
				cardContext.getPlayersPanel().get(activePlayerIndex).unmarkPlayedCardsPanel();
			}
			
    		boolean isPanelCrossed = false;
    		Point screenPoint = new Point(e.getXOnScreen(), e.getYOnScreen());
    		String choosedPlayerName = null;
    		
            for (int activePlayerIndex : availablePlayersIndexes) {
            	Point finalPoint = new Point(screenPoint);
            	JPanel playerPanel = cardContext.getPlayersPanel().get(activePlayerIndex).getPlayedCardsPanel();
            	SwingUtilities.convertPointFromScreen(finalPoint, playerPanel);
            	if (playerPanel.contains(finalPoint)) {
            		System.out.println("PANEL CROSSED");
            		choosedPlayerName = cardContext.getPlayersPanel().get(activePlayerIndex).getPlayerName();
            		isPanelCrossed = true;
            		break;
            	}
            }
            
            if (isPanelCrossed == false) {
            	cardContext.getLayeredPane().remove(cardData.getCard());
            	cardContext.getActivePlayerCardsPanel().add(cardData.getCard());
            	cardData.getCard().setBounds(cardData.getPositionX(), cardData.getPositionY(), cardData.getWidth(), cardData.getHeight());
        	}
            else if (!CardMouseListener.isCardUsed) {
            	CardMouseListener.isCardUsed = true;
            	otherCardMouseMotionListener.removeMouseMotionListener();
            	fadeOutCardEffect(cardData.getCard());
            	int cardIndex = cardData.getIndex();
            	controller.chosenPlayerAndCard(choosedPlayerName, currentPlayerIndex, cardIndex);
            }  
            cardContext.getLayeredPane().revalidate();
            cardContext.getLayeredPane().repaint();
		}
	
	private Image makeTransparentImage(ImageIcon icon, float alpha) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(icon.getImage(), 0, 0, null);
        g2d.dispose();

        return image;
    }
	
	private void fadeOutCardEffect(JLabel card) {
		Timer timer = new Timer(50, null);
        final float[] alpha = {1.0f};

        timer.addActionListener(e -> {
            alpha[0] -= 0.05f;
            if (alpha[0] <= 0) {
                ((Timer) e.getSource()).stop();
                cardContext.getLayeredPane().remove(card);
                return;
            }
            card.setIcon(new ImageIcon(makeTransparentImage((ImageIcon) card.getIcon(), alpha[0])));
            card.revalidate();
            card.repaint();
        });
        
        timer.start();
	}
}
