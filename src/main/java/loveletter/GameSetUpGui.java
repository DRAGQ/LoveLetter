package loveletter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameSetUpGui {
	
	private JFrame frame;
	private GameController controller;
	private JPanel headerPanel, numberOfPlayersPanel, buttonPanel, startPanel, boxPanel, centerPanel, textFieldPanel, warningPanel;
	private Integer[] numberOfPlayersArray;
	private int numberOfPlayers;
	private JComboBox<Integer> comboBoxNumberOfPlayers;
	private ArrayList<JTextField> playerNamesField;
	
	GameSetUpGui(GameController controller) {
		this.controller = controller;
		initializeGui();
	}
	
	private void initializeGui() {
		JLayeredPane layeredPane = new JLayeredPane();
		
		createFrame();
		initializePanels();
		
		this.playerNamesField = new ArrayList<>();
		
		createAndAddBackgroundImage(layeredPane);
		numberOfPlayerInputs();
		this.numberOfPlayers = numberOfPlayersArray[0];
		
		showPlayersField();	
		setupPanels();
		
		startGameButton();
		
		JLabel loveLetterLabel = new JLabel("Love Letter");
		customFont(loveLetterLabel);
		
		addIntoPanels(loveLetterLabel);	
		setOpaque();
		
		this.frame.add(layeredPane);
		layeredPane.add(startPanel, JLayeredPane.MODAL_LAYER);
		this.frame.setVisible(true);
	}
	
	private void createFrame() {
		this.frame = new JFrame("Love Letter");
		this.frame.setSize(440, 480);
		this.frame.setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
	}
	
	private void initializePanels() {
		this.headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.startPanel = new JPanel(new BorderLayout());
		this.boxPanel = new JPanel();
		this.centerPanel = new JPanel();
		this.textFieldPanel = new JPanel();
		this.buttonPanel = new JPanel();
		this.warningPanel = new JPanel();
	}
	
	private void createAndAddBackgroundImage(JLayeredPane layeredPane) {
		ImageIcon background = new ImageIcon(getClass().getResource("/backgroundImage/bcgSetUp.png"));
		JLabel backg = new JLabel(background);
		backg.setBounds(0, 0, 440, 450);
		layeredPane.add(backg, JLayeredPane.DEFAULT_LAYER);
	}
	
	private void numberOfPlayerInputs() {
		numberOfPlayersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JLabel auxiliaryLabel = new JLabel("Number of players.");
		auxiliaryLabel.setFont( new Font("Arial",Font.BOLD,15));
		auxiliaryLabel.setForeground(new Color(255, 255, 50));
		this.numberOfPlayersArray = new Integer[] {2, 3, 4};
		this.comboBoxNumberOfPlayers = new JComboBox<>(numberOfPlayersArray);
		this.comboBoxNumberOfPlayers.addActionListener(e -> {
			this.boxPanel.remove(this.warningPanel);
			revalidatePlayersField();
		});
		
		this.comboBoxNumberOfPlayers.setBackground(new Color(255, 255, 50));
		
		numberOfPlayersPanel.add(auxiliaryLabel);
		numberOfPlayersPanel.add(this.comboBoxNumberOfPlayers);
		numberOfPlayersPanel.setPreferredSize(new Dimension(465,15));
	}
	
	private void setupPanels() {
		this.textFieldPanel.setOpaque(false);
		
		this.textFieldPanel.setLayout(new BoxLayout(this.textFieldPanel, BoxLayout.Y_AXIS));
		this.centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		
		this.headerPanel.setPreferredSize(new Dimension(465,60));
		startPanel.setBounds(0, 0, 420, 450);
	}
	
	private void startGameButton() {
		JButton startGameButton = new JButton("Start Game");
		startGameButton.addActionListener(e -> {
			if (checkValidNames()) {
				newGame();
			}
		});
		
		startGameButton.setFont( new Font("Arial",Font.BOLD,13));
		startGameButton.setBackground(Color.orange);
		startGameButton.setPreferredSize(new Dimension(100, 30));
		this.buttonPanel.add(startGameButton);
	}
	
	private void customFont(JLabel label) {
		try (InputStream is = CustomTextFont.class.getResourceAsStream("/textFont/MeowScript-Regular.ttf")) {
			if (is == null) {
				System.err.println("Font file not found in resources!");
			}
		    Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(72f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(customFont);
		    
		    label.setFont(customFont);
		    label.setForeground(new Color(255, 255, 123));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	private void addIntoPanels(JLabel loveLetterLabel) {
		this.centerPanel.add(this.textFieldPanel);
		this.headerPanel.add(loveLetterLabel);
		this.boxPanel.add(headerPanel);
		this.boxPanel.add(numberOfPlayersPanel);
		this.boxPanel.add(Box.createVerticalStrut(10));
		this.boxPanel.add(this.centerPanel);
		this.boxPanel.add(Box.createVerticalStrut(10));
		this.boxPanel.add(buttonPanel);
		this.startPanel.add(boxPanel, BorderLayout.CENTER);
	}
	
	private void setOpaque() {
		this.startPanel.setOpaque(false);
		this.boxPanel.setOpaque(false);
		this.headerPanel.setOpaque(false);
		this.numberOfPlayersPanel.setOpaque(false);
		this.centerPanel.setOpaque(false);
		this.buttonPanel.setOpaque(false);
		this.warningPanel.setOpaque(false);
	}
	
	public void newGame() {
		this.frame.dispose();
		new GameGui(this.controller, playerNamesField);
	}
	
	private void revalidatePlayersField() {
		this.textFieldPanel.removeAll();
		this.playerNamesField.clear();
		this.numberOfPlayers = (int) comboBoxNumberOfPlayers.getSelectedItem();
		showPlayersField();
		this.frame.revalidate();
		this.frame.repaint();
	}
	
	private boolean checkValidNames() {
		JLabel warningLabel = new JLabel();
		warningLabel.setForeground(new Color(0, 0, 153));
		warningLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		warningPanel.removeAll();
		warningPanel.add(warningLabel);
		this.boxPanel.remove(warningPanel);
		this.boxPanel.add(warningPanel, boxPanel.getComponentCount() - 1);
		
		for (int i = 0; i < (this.numberOfPlayers - 1); i++) {
			String player1Name = playerNamesField.get(i).getText();
			for (int j = i + 1; j < this.numberOfPlayers; j++) {
				String player2Name = playerNamesField.get(j).getText();
				String errorMessage = isValidNameLength(player1Name, player2Name);
				if (errorMessage != null) {
					warningLabel.setText(errorMessage);
					frame.revalidate();
					frame.repaint();
					return false;
					}
					
				errorMessage = isUniqueName(player1Name, player2Name);
				if (errorMessage != null) {
					warningLabel.setText(errorMessage);
					frame.revalidate();
					frame.repaint();
					return false;
					}
			}
		}
		return true;
	}

		private String isValidNameLength(String player1Name, String player2Name) {
			if ((player1Name.length() < 2 || player2Name.length() < 2) || (player1Name.length() > 20 || player2Name.length() > 20)) {
				return "Each name must have a min. of two characters and a max. of 20";
			}
			return null;
		}
		
		private String isUniqueName(String player1Name, String player2Name) {
			if (player1Name.equals(player2Name)) {
				return "Each player name must be unique!";
			}
			return null;
		}
	
	private void showPlayersField() {
		this.centerPanel.setPreferredSize(new Dimension(100, (40 * this.numberOfPlayers) + (this.numberOfPlayers * 5)));
		for (int i = 0; i < this.numberOfPlayers; i++) {
			RoundedTextField playerNameField = new RoundedTextField(15);
			playerNameField.setFont( new Font("Elephant",Font.ITALIC,15));
			playerNameField.setPreferredSize(new Dimension(200, 40));
			this.playerNamesField.add(playerNameField);
			this.textFieldPanel.add(playerNameField);
			this.textFieldPanel.add(Box.createVerticalStrut(10));
		}
	}
}
