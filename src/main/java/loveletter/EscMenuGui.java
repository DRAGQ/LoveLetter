package loveletter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class EscMenuGui {
	
	private GameGui gui;
	private JFrame frame;
	private JLayeredPane layeredPane;
	private JPanel menuPanel;
	private static boolean isMenuOpened;
	private JLabel zoomedCardImage, exitImage;
	private List<Component> componentsToHide;
	private boolean isCardAction;
	
	EscMenuGui(GameGui gui) {
		this.gui = gui;
		this.frame = gui.getFrame();
		this.layeredPane = gui.getLayeredPane();
		this.componentsToHide = new ArrayList<>();
		this.isCardAction = false;
		EscMenuGui.isMenuOpened = false;
		addListenerforEsc();
	}
	
	private void addListenerforEsc() {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
	            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	            	if (!EscMenuGui.isMenuOpened) {
	            		if (CardZoomedListener.isCardOpened) {
	            			CardZoomedListener.isCardOpened = false;
		            		layeredPane.remove(this.exitImage);
		            		layeredPane.remove(this.zoomedCardImage);
		            		gui.getShadowPane().decreaseCount();
		            	}
		            	else {
		            		EscMenuGui.isMenuOpened = true;
		            		createMenu();
		            		gui.getShadowPane().addCount();
		            		hideComponents();
		            	}
	            	}
	            	else {
	            		resumeGame();
	            	}
	            	layeredPane.revalidate();
	    			layeredPane.repaint();
	            }
	            return false;
	        });
	}
	
	private void createMenu() {
		menuPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setColor(new Color(0, 30, 130, 100));
		        g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		int xPosition = layeredPane.getWidth() / 2 - 200;
		int yPosition = layeredPane.getHeight() / 2 - 117;

		menuPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
		menuPanel.setLayout(null);
		menuPanel.setBounds(xPosition, yPosition, 400, 235);
		
		JButton resumeButton = new EscMenuButton("RESUME");
		resumeButton.setBounds(75, 50, 250, 35);
		resumeButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resumeGame();
			}
		});	
		
		
		JButton startNewGame = new EscMenuButton("NEW GAME");
		startNewGame.setBounds(75, 100, 250, 35);
		startNewGame.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.restartGame();
				resumeGame();
			}
		});	
		
		
		JButton exitButton = new EscMenuButton("EXIT");
		exitButton.setBounds(75, 150, 250, 35);
		exitButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		menuPanel.add(startNewGame);
		menuPanel.add(resumeButton);
		menuPanel.add(exitButton);
		layeredPane.add(menuPanel, JLayeredPane.DRAG_LAYER);
	}
	
	private void resumeGame() {
		EscMenuGui.isMenuOpened = false;
		layeredPane.remove(this.menuPanel);

		showComponents();
		if (!isCardAction){
			gui.getShadowPane().decreaseCount();
		}
		layeredPane.revalidate();
		layeredPane.repaint();
	}
	
	public boolean getIsMenuOpened() {
		return isMenuOpened;
	}
	
	public void setIsCardAction(boolean isCardAction) {
		this.isCardAction = isCardAction;
	}
	
	public void zoomedCardIsOpened(JLabel zoomedCardImage, JLabel exitImage) {
		this.zoomedCardImage = zoomedCardImage;
		this.exitImage = exitImage;
	}

	public void addComponentToHide(Component comp) {
	    componentsToHide.add(comp);
	}
	
	public void clearComponentToHide() {
	    componentsToHide.removeAll(componentsToHide);
	}

	private void hideComponents() {
	    for (Component c : componentsToHide) {
	        c.setVisible(false);
	    }
	}

	private void showComponents() {
	    for (Component c : componentsToHide) {
	        c.setVisible(true);
	    }
	}
	
}
