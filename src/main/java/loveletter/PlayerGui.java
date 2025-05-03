package loveletter;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerGui{
	private String playerName, characterColor;
	private int numberOfHearts;
	private JPanel playerPanel, playerBackCardPanel, wrapperNameAndHeartPanel, characterPanel, heartPanel;
	private PlayerPlayedCardsPanel playedCardsPanel;
	private JLabel nameLabel;
	private ArrayList<JLabel> heartLabels;
	private ImageIcon fullHeartIcon;
	private boolean isFirstBackCard;
	
	PlayerGui(String name, int score, int gameWidth, int gapsBetweenPanels, int numberOfHearts, String characterColor) {
		this.playerName = name;
		this.numberOfHearts = numberOfHearts;
		this.characterColor = characterColor;
		this.isFirstBackCard = true;
		
		int panelWidth = gameWidth / 4 - gapsBetweenPanels;
		
		initializePanels(panelWidth);
		initializeLabels();
		initializeIcons();
		assembleLayout();
		setBackgroundColor();
	}
	
	private void initializePanels(int panelWidth) {
		this.playerPanel = createPanel(BoxLayout.Y_AXIS, new Dimension(panelWidth, 500), Color.decode("#e1c6a9"));
		this.heartPanel = createPanel(BoxLayout.X_AXIS, null, null);
		this.wrapperNameAndHeartPanel = createPanel(BoxLayout.Y_AXIS, new Dimension(250, 60), Color.decode("#806a50"));
		this.playerBackCardPanel = createPanel(666, new Dimension(60, 42), null);
		this.playerBackCardPanel.setBounds(205, 86, 60, 42);
		this.playedCardsPanel = new PlayerPlayedCardsPanel(new Dimension((panelWidth), 210));
		this.playedCardsPanel.setPreferredSize(new Dimension(panelWidth, 210));
	}
	
	private void initializeLabels() {
        this.nameLabel = CustomTextFont.createFont(playerName);
    }
	
	private void initializeIcons() {
        fullHeartIcon = new ImageIcon(getClass().getResource("/hearts/fullHeart.png"));
        addCharacter();
        addEmptyHearts();
    }
	
	private JPanel createPanel(int axis, Dimension size, Color borderColor) {
        JPanel panel = new JPanel();
        if (axis != 666) {
        	panel.setLayout(new BoxLayout(panel, axis));
        } else {
        	panel.setLayout(null);
        }
       
        
        if (size != null) {
	        panel.setPreferredSize(size);
	        panel.setMaximumSize(size);
        }
        
        if (borderColor != null) {
            panel.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        }
        return panel;
    }
	
	private void assembleLayout() {
		JPanel centerPanel = new CenterPanelForLabel(this.nameLabel, 250, 25);
	    centerPanel.setOpaque(false);
	    
	    this.wrapperNameAndHeartPanel.add(centerPanel);
	    this.wrapperNameAndHeartPanel.add(this.heartPanel);
	    this.characterPanel.add(this.playerBackCardPanel);
	    
	    this.playerPanel.add(this.characterPanel);
	    this.playerPanel.add(this.wrapperNameAndHeartPanel);
	    this.playerPanel.add(playedCardsPanel);

	    this.playerPanel.revalidate();
	    this.playerPanel.repaint();
	}
	
	private void setBackgroundColor() {
		this.playerPanel.setOpaque(false);
		this.wrapperNameAndHeartPanel.setBackground(Color.decode("#9b1434"));
		this.heartPanel.setOpaque(false);
		this.playerBackCardPanel.setOpaque(false);
		this.playedCardsPanel.setOpaque(false);
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public JPanel getPlayerPanel() {
		return this.playerPanel;
	}
	
	public JPanel getPlayedCardsPanel() {
		return this.playedCardsPanel;
	}
	
	public void markPlayedCardsPanel() {
		this.playedCardsPanel.setOpaque(false);
		this.playedCardsPanel.setTransparency(0.5f);
	}
	
	public void lostPlayedCardsPanel() {
		this.playedCardsPanel.setOpaque(true);
		this.playedCardsPanel.setTransparency(1f);
	}
	
	public void unmarkPlayedCardsPanel() {
		this.playedCardsPanel.setOpaque(false);
		this.playedCardsPanel.setTransparency(0f);
	}
	
	public void updatePlayerScore(int score) {
		heartLabels.get(score).setIcon(fullHeartIcon);
	}
	
	private void addCharacter() {
		characterPanel = new JPanel();
		characterPanel.setLayout(null);
		characterPanel.setBounds(0, 0, 250, 128);
		characterPanel.setPreferredSize(new Dimension(250, 128));
		characterPanel.setMaximumSize(new Dimension(250, 128));
		characterPanel.setOpaque(false);
		
		ImageIcon circleIcon = new ImageIcon(getClass().getResource("/characters/circle3.png"));
		ImageIcon characterIcon = new ImageIcon(getClass().getResource("/characters/" + characterColor + "Character.png"));
		JLabel circleLabel = new JLabel(circleIcon);
		JLabel characterLabel = new JLabel(characterIcon);

		circleLabel.setBounds(61, 0, 128, 128);
		characterLabel.setBounds(81, 12, 100, 100);
		characterPanel.add(characterLabel);
		characterPanel.add(circleLabel);
		this.playerPanel.revalidate();
		this.playerPanel.repaint();
	}
	
	private void addEmptyHearts() {
		heartLabels = new ArrayList<>();
		ImageIcon emptyHeartIcon = new ImageIcon(getClass().getResource("/hearts/emptyHeart.png"));
		for (int i = 0; i < numberOfHearts; i++) {
		    JLabel heartLabel = new JLabel(emptyHeartIcon);
		    heartLabels.add(heartLabel);
		    this.heartPanel.setBackground(null);
		    this.heartPanel.add(heartLabel);
		}
	}
	
	public void resetPlayerHearts() {
		this.heartPanel.removeAll();
		addEmptyHearts();
	}
	
	

	public void addBackCard() {
	    ImageIcon icon = new ImageIcon(getClass().getResource("/cardsUI/backCard.png"));
	    JLabel	backCard = new JLabel(icon);
	    int x = isFirstBackCard ? 0 : icon.getIconWidth() - 15;
	    backCard.setBounds(x, 0, icon.getIconWidth(), icon.getIconHeight());
	    isFirstBackCard = false;
	    this.playerBackCardPanel.add(backCard);
	    this.playerBackCardPanel.setComponentZOrder(backCard, 0);
	    this.playerBackCardPanel.revalidate();
	    this.playerBackCardPanel.repaint();
	}
	
	public void removeBackCard() {
		playerBackCardPanel.remove(0);
	}
	
	public void setIsFirstCard() {
		isFirstBackCard = true;
	}
	
	public void removeAllBackCards() {
		playerBackCardPanel.removeAll();
		setIsFirstCard();
	}
	
	public void removeAllPlayedCards() {
		playedCardsPanel.removeAll();
	}
	
	public JLabel addPlayedCard(String cardName, int numberOfPlayedCards) {
		ImageIcon image = new ImageIcon(getClass().getResource("/cardsUI/" + cardName + ".png"));
	    JLabel	playedCard = new JLabel(image);
	    System.out.println("HEIGHT: " + playedCard.getIcon().getIconHeight());
	    int x = (numberOfPlayedCards - 1) * (playedCard.getIcon().getIconWidth() - 100);
	    playedCard.setBounds(x, 0, playedCard.getIcon().getIconWidth(), playedCard.getIcon().getIconHeight());
		playedCardsPanel.add(playedCard);
		playedCardsPanel.setComponentZOrder(playedCard, 0);
		playedCardsPanel.repaint();
		return playedCard;
	}

}
