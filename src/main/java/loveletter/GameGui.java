package loveletter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class GameGui {

	private GameController controller;
	private EscMenuGui escMenu;
	private JFrame frame;
	private JLayeredPane layeredPane;
	private JPanel numberOfCardsPanel, firstThreeCardsPanel, playersTopPanel, playersBottomPanel, playerNamePanel, activePlayerCardsPanel, scoreboardPanel;
	private JPanel guardGuessPanel, cardsToComparePanel;
	private ShadowPane shadowPane;
	private JLabel bcgNameImage, numberOfCardsInt, secretCard, playerLabel, card1, card2, backCard1, backCard2, scoreboardImage;
	private JButton showHideButton;
	private String activePlayerName, card1Name, card2Name;
	private int gameHeight, gameWidth, numberOfPlayers, currentPlayerIndex, card1Rank, availablePlayersForCardIndex;
	private static boolean isSecretCard;
	private List <PlayerGui> playersPanel;
	private ArrayList<JTextField> playerNamesField;
	private List<Integer> availablePlayersForFirstCard, availablePlayersForSecondCard;
	private List<JPanel> playersCardsList;
	
	GameGui(GameController controller, ArrayList<JTextField> playerNamesField) {
		this.frame = new JFrame("Love Letter");
		this.controller = controller;

		initializePanels();
		initializePlayersInformations(playerNamesField);
		
		setGuiController(this);
		setupGui();
		createAndAddBackgroundImages();
	}
	
	private void initializePanels() {
		this.layeredPane = new JLayeredPane();
		this.playersTopPanel = new JPanel();
		this.playersBottomPanel = new JPanel();
		this.numberOfCardsPanel = new JPanel();
		this.firstThreeCardsPanel = new JPanel();
	}
	
	private void initializePlayersInformations(ArrayList<JTextField> playerNamesField) {
		this.playerNamesField = playerNamesField;
		this.numberOfPlayers = playerNamesField.size();
		this.playersPanel = new ArrayList<>();
		this.numberOfCardsInt = CustomTextFont.createFont("16");
	}
	
	private void createAndAddBackgroundImages() {
		JLabel bcgImage = createImageLabel("bcgMainGame", 0, 0, false);
		JLabel bcgNumberOfCards = createImageLabel("numberOfCardBackground", gameWidth - 470, 550, false);
		bcgNameImage = createImageLabel("turnNameBcg", 30, 700, false);
		JLabel bcgPlayerCards =  createImageLabel("cardBcg", 670, 590, false);
		
		this.layeredPane.add(bcgImage, JLayeredPane.DEFAULT_LAYER);
		this.layeredPane.add(bcgNumberOfCards, JLayeredPane.PALETTE_LAYER);
		this.layeredPane.add(bcgNameImage, JLayeredPane.PALETTE_LAYER);
		this.layeredPane.add(bcgPlayerCards, JLayeredPane.PALETTE_LAYER);
		updatePane();
	}
	
	public void showRoundNumber(Object roundNumber) {
		if (roundNumber instanceof Integer) {
			JLabel roundImage = createImageLabel("roundNumber", 0, 0, true);
			JLabel roundNumberLabel = CustomTextFont.createFont(roundNumber + ". Round");
			roundNumberLabel.setBounds(920, 480, 200, 30);
			
			this.shadowPane.addCount();
			this.layeredPane.add(roundImage, JLayeredPane.POPUP_LAYER);
			this.layeredPane.add(roundNumberLabel, JLayeredPane.DRAG_LAYER);
			
			escMenu.addComponentToHide(roundImage);
			escMenu.addComponentToHide(roundNumberLabel);

			updatePane();
			
			Timer timer = new Timer(2500, e -> {
				this.shadowPane.decreaseCount();
				layeredPane.remove(roundImage);
				layeredPane.remove(roundNumberLabel);
				escMenu.clearComponentToHide();
				
				updatePane();
			});
			timer.setRepeats(false);
			timer.start();
		}
	}
	
	private JLabel createImageLabel(String imageName, int x, int y, boolean isMiddle) {
		ImageIcon icon = new ImageIcon(getClass().getResource("/backGroundImage/" + imageName + ".png"));
		JLabel image = new JLabel(icon);
		int xPosition = !isMiddle ? x : (gameWidth / 2) - (icon.getIconWidth() / 2);
		int yPosition = !isMiddle ? y : (gameHeight / 2) - (icon.getIconHeight() / 2);
		image.setBounds(xPosition, yPosition, icon.getIconWidth(), icon.getIconHeight());
		return image;
	}
	
	private void setGuiController(GameGui gui) {
		controller.setGuiController(gui);
	}

	private void setupGui() {
		createFullscreenFrame();
		escMenu = new EscMenuGui(this);
		this.availablePlayersForCardIndex = 0;
		
		setupMainPanels();
		setupNumberOfCardsPanel();
		this.frame.setVisible(true); // important this comes before shadowPane setup
		this.shadowPane = new ShadowPane(layeredPane);
		
		addSetupIntoLayeredPane();
		setUpForTwoPlayers();	
		
		startGameBackend();
		createPlayersPanel();
		createShowHideButton();

		this.frame.revalidate();
		this.frame.repaint();
	}
	
	private void createFullscreenFrame() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();
        gameWidth = screenBounds.width;
        gameHeight = screenBounds.height;
        this.frame.setSize(gameWidth, gameHeight);
		this.frame.setUndecorated(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.add(layeredPane);
	}
	
	private void setupMainPanels() {
		this.playersTopPanel.setLayout(new GridBagLayout());;
		this.playersTopPanel.setBounds(0, 0, gameWidth, 500);
		this.playersTopPanel.setOpaque(false);
		
		this.playersBottomPanel.setLayout(null);
		this.playersBottomPanel.setBounds(0, 500, gameWidth, gameHeight - 500);
		this.playersBottomPanel.setOpaque(false);
	}
	
	private void setupNumberOfCardsPanel() {
		JLabel numberOfCardsLabel = CustomTextFont.createFont("Number of cards:");
		this.numberOfCardsPanel.setBounds(gameWidth - 415, gameHeight - 400, 250, 30);
		this.numberOfCardsPanel.add(numberOfCardsLabel);
		this.numberOfCardsPanel.setOpaque(false);
		this.numberOfCardsInt.setBounds(gameWidth - 297, gameHeight - 360, 250, 30);
	}
	
	private void addSetupIntoLayeredPane() {
		this.layeredPane.add(this.shadowPane, JLayeredPane.MODAL_LAYER);
		this.layeredPane.add(this.numberOfCardsPanel, JLayeredPane.MODAL_LAYER);
		this.layeredPane.add(this.numberOfCardsInt, JLayeredPane.MODAL_LAYER);
		this.layeredPane.add(this.playersTopPanel, JLayeredPane.PALETTE_LAYER);
		this.layeredPane.add(this.playersBottomPanel, JLayeredPane.PALETTE_LAYER);
	}
	
	private void setUpForTwoPlayers() {
		if (numberOfPlayers ==  2) {
			JLabel bcgFirstThreeCards = createImageLabel("roll", gameWidth - 480, 850, false);	
			
			this.firstThreeCardsPanel.setLayout(new BoxLayout(firstThreeCardsPanel, BoxLayout.X_AXIS));
			this.firstThreeCardsPanel.setBounds(gameWidth - 425,gameHeight - 195, 270, 126 );
			this.layeredPane.add(firstThreeCardsPanel, JLayeredPane.PALETTE_LAYER);
			this.layeredPane.add(bcgFirstThreeCards, JLayeredPane.PALETTE_LAYER);
		}
	}
	
	private void startGameBackend() {
		controller.startGame(this.numberOfPlayers, this.playerNamesField);
	}
	
	private void createPlayersPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		int gapsBetweenPanels = 3;
		gbc.insets = new Insets(0, gapsBetweenPanels, 0, gapsBetweenPanels);
		gapsBetweenPanels *= this.numberOfPlayers * 2;
		
		for (int i = 0; i < this.numberOfPlayers; i++) {
			gbc.gridx = i;
			gbc.gridy = 0;
			String characterColor = i == 0 ? "black" : i == 1 ? "blue" : i == 2 ? "green" : "red";
			
			String playerName = this.playerNamesField.get(i).getText();
			int score = controller.getPlayerScore(playerName);
			int numberOfHearts = this.numberOfPlayers == 2 ? 7 : this.numberOfPlayers == 3 ? 5 : 4;
			PlayerGui playerGui = new PlayerGui(playerName, score, gameWidth, gapsBetweenPanels, numberOfHearts, characterColor);
			this.playersPanel.add(playerGui);
			this.playersTopPanel.add(playerGui.getPlayerPanel(), gbc);
		}
	}
	
	private void createShowHideButton() {
		this.showHideButton = new JButton("SHOW");
		this.showHideButton.setBounds(870, 1000, 200, 40);
		this.showHideButton.addActionListener(e -> {
			if (this.showHideButton.getText().equals("SHOW")) {
				card1.setVisible(true);
				card2.setVisible(true);
				this.backCard1.setVisible(false);
				this.backCard2.setVisible(false);
				this.showHideButton.setText("HIDE");
			} else {
				card1.setVisible(false);
				card2.setVisible(false);
				this.backCard1.setVisible(true);
				this.backCard2.setVisible(true);
				this.showHideButton.setText("SHOW");
			}
		});
		layeredPane.add(showHideButton);
	}
	
	public void restartGame() {
		controller.resetGame(playerNamesField);
		removeSecretCard(true);
		this.shadowPane.resetCount();
		escMenu.setIsCardAction(false);
		if (guardGuessPanel != null) { layeredPane.remove(guardGuessPanel); }
		this.layeredPane.remove(playerNamePanel);
		for (PlayerGui player : playersPanel) {
			player.unmarkPlayedCardsPanel();
			player.resetPlayerHearts();
		}
		removeScoreboard();
	}
	
	public void setSecretCard(Object shouldShow) {
		GameGui.isSecretCard = true;
		ImageIcon icon = new ImageIcon(getClass().getResource("/cardsUI/secretCard.png"));
		secretCard = new JLabel(icon);
		JLabel informLabel = new JLabel("Secret Card");
		JPanel secretCardPanel = new JPanel();
		secretCardPanel.setBackground(Color.GREEN);
		secretCardPanel.add(informLabel);
		
		secretCard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				layeredPane.add(secretCardPanel, JLayeredPane.MODAL_LAYER);
				updatePane();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				layeredPane.remove(secretCardPanel);
				updatePane();
			}
		});
		secretCardPanel.setBounds(gameWidth - 365, 790, 150, 30);
		secretCard.setBounds(gameWidth - 312, 752, icon.getIconWidth(), icon.getIconHeight());
		layeredPane.add(secretCard, JLayeredPane.MODAL_LAYER);
	}
	
	public void removeSecretCard(Object isTrue) {
		if (GameGui.isSecretCard) {
			layeredPane.remove(secretCard);
			GameGui.isSecretCard = false;
			updatePane();
		}
	}
	
	public void updateNumberOfCards(Object number) {
		this.numberOfCardsInt.setText(String.valueOf(number));
	}
	
	public void showFirstThreeCards(Object firstThreeCards) {
		if (firstThreeCards instanceof List <?>) {
			@SuppressWarnings("unchecked")
			List <String> threeCards = (List <String>) firstThreeCards;
			firstThreeCardsPanel.removeAll();
			for (String cardName : threeCards) {
				ImageIcon image = new ImageIcon(getClass().getResource("/smallCards/" + cardName + ".png"));
				JLabel labelCard = new JLabel(image);
				showZoomedCard(labelCard, cardName);
				firstThreeCardsPanel.add(labelCard);
			}
		} else {
			System.out.println("Invalid input: firstThreeCards is not instanceof List <String>");
		}
	}
	
	public void updatePlayersBackSideCards(Object receivedPlayer) {
		if (receivedPlayer instanceof Player) {
	        Player player = (Player) receivedPlayer;
	        currentPlayerIndex = player.getIndex();
	        playersPanel.get(player.getIndex()).addBackCard();
	        updatePane();
	    } else {
	        System.out.println("Invalid input: receivedPlayer is not instanceof Player");
	    }
	};

	public void drawCardsGui(Object receivedPlayer) {
		if (receivedPlayer instanceof Player) {
			Player player = (Player) receivedPlayer;
			this.activePlayerName = player.getName();
			card1Name = player.getCard(0).getPattern();
			card2Name = player.getCard(1).getPattern();
			card1Rank = player.getCard(0).getRank();
			renderPlayerTurnSetUp(this.activePlayerName, card1Name, card2Name);
		} else {
	        System.out.println("Invalid input: receivedPlayer is not instanceof Player");
	    }
	}
	
	private void renderPlayerTurnSetUp(String activePlayerName, String card1Name, String card2Name) {
		this.playerLabel = CustomTextFont.createFont(activePlayerName);
		this.playerLabel.setBounds(500,20,100,100);
		this.activePlayerCardsPanel = new JPanel();
		this.activePlayerCardsPanel.setLayout(null);
		
		int cardWidth = 150;
	    int cardHeight = 210;
	    
	    this.backCard1 = createJLabelForCard("backCardToggle", 0, 0);
		this.backCard2 = createJLabelForCard("backCardToggle", cardWidth + 20, 0);
	    
		this.card1 = createJLabelForCard(card1Name, 0, 0);
		this.card2 = createJLabelForCard(card2Name, cardWidth + 20, 0);
		
		this.card1.setVisible(false);
		this.card2.setVisible(false);
		this.showHideButton.setText("SHOW");
		
		JLabel[] cards = {card1, card2};
		String[] cardNames = {card1Name, card2Name};
		int[] xPositions = {0, cardWidth + 20};
		System.out.println("LALALA");
		System.out.println(this.availablePlayersForFirstCard);
		List<List<Integer>> availablePlayers = List.of(this.availablePlayersForFirstCard, this.availablePlayersForSecondCard);
		CardMouseMotionListener[] motionListeners = new CardMouseMotionListener[cards.length];
		CardListenerContext cardContext = new CardListenerContext(this.layeredPane, this.activePlayerCardsPanel, this.playersPanel);
		
		for (int i = 0; i < cards.length; i++) {
			motionListeners[i] = new CardMouseMotionListener(cards[i], this.activePlayerCardsPanel, this.layeredPane);
		}
		
		for (int i = 0; i < cards.length; i++) {
			CardData cardData = new CardData(cards[i], cardNames[i], i, xPositions[i], 0, cardWidth, cardHeight);
			cards[i].addMouseMotionListener(motionListeners[i]);
			CardMouseMotionListener otherMouseListener = (i == 0 ? motionListeners[1] : motionListeners[0]);
			cards[i].addMouseListener(new CardMouseListener(cardData, cardContext, this.controller, this.currentPlayerIndex, availablePlayers.get(i), otherMouseListener));
			cards[i].addMouseListener(new CardZoomedListener(cardNames[i], this.layeredPane, this.shadowPane, this.escMenu));
		}
		int yPosition = this.playersBottomPanel.getHeight() / 2 - this.card1.getHeight() / 2;
		int xPosition = this.playersBottomPanel.getWidth() / 2 - this.card1.getWidth();

		this.playerNamePanel = new CenterPanelForLabel(this.playerLabel, 265,  40);
		this.playerNamePanel.setBounds(120, gameHeight - 250, 265, 30);
		this.playerNamePanel.setOpaque(false);

		this.layeredPane.add(playerNamePanel, JLayeredPane.MODAL_LAYER);

		this.activePlayerCardsPanel.setOpaque(false);
		this.activePlayerCardsPanel.setBounds(xPosition - 10, yPosition, cardWidth * 2 + 20, cardHeight); 
		this.playersBottomPanel.add(this.activePlayerCardsPanel);
		this.playersBottomPanel.revalidate();
		this.playersBottomPanel.repaint();
	}
	
	public void showPlayedCard(Object receivedPlayer) {
		if (receivedPlayer instanceof Player) {
			Player player = (Player) receivedPlayer;
			String lastPlayedCardName = player.getLastPlayedCard();
			PlayerGui playerGui = playersPanel.get(player.getIndex());
			JLabel playedCardLabel = playerGui.addPlayedCard(lastPlayedCardName, player.getSizePlayedCards());
			showZoomedCard(playedCardLabel, lastPlayedCardName);
			
			if (player.getLostRound()) {
				playerGui.lostPlayedCardsPanel();
				playerGui.removeAllBackCards();
				if (player.getName().equals(activePlayerName)) {
					this.activePlayerCardsPanel.removeAll();
				}
			}
			else {
				System.out.println("REMOVED");
				playerGui.removeBackCard();
			}
			updatePane();
		} else {
	        System.out.println("Invalid input: receivedPlayer is not instanceof Player");
	    }
	}
	
	public void clearPlayerDeck(Object Void) {
			layeredPane.remove(playerNamePanel);
			playersBottomPanel.removeAll();
			for (PlayerGui player : playersPanel) {
				player.unmarkPlayedCardsPanel();
			}
			updatePane();
	}
	
	public void saveAllActivePlayersForEachCard(Object allActivePlayers) {
		if (allActivePlayers instanceof ArrayList<?>) {
			@SuppressWarnings("unchecked")
			ArrayList<Integer> availablePlayers = (ArrayList<Integer>) allActivePlayers;
			
			if (this.availablePlayersForCardIndex == 0) {
				this.availablePlayersForFirstCard = new ArrayList<>();
				this.availablePlayersForFirstCard = availablePlayers;
				this.availablePlayersForCardIndex = 1;
			}
			else if (this.availablePlayersForCardIndex == 1) {
				this.availablePlayersForSecondCard = new ArrayList<>();
				this.availablePlayersForSecondCard  = availablePlayers;
				this.availablePlayersForCardIndex = 0;
			}
		} else {
	        System.out.println("Invalid input: allActivePlayers is not instanceof ArrayList");
	    }
	    
	}

	private JLabel createJLabelForCard(String cardName, int positionX, int positionY) {
		ImageIcon icon = new ImageIcon(getClass().getResource("/cardsUI/" + cardName + ".png"));
		JLabel card = new JLabel(icon);
		card.setBounds(positionX, positionY, icon.getIconWidth(), icon.getIconHeight());
		this.activePlayerCardsPanel.add(card);
		return card;
	}
	
	private void createCardsPanel(JPanel cardsPanel, int numberOfCards, int cardWidth, int cardHeight) {
		cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.X_AXIS));
		int cardsPanelWidth = numberOfCards * cardWidth;
		int namePanelHeight = 30;
		int xPosition = (gameWidth / 2) - (cardsPanelWidth / 2);
		int yPosition = (gameHeight / 2) - ((cardHeight + namePanelHeight) / 2);
		
		cardsPanel.setBounds(xPosition, yPosition, cardsPanelWidth, cardHeight + namePanelHeight);
		cardsPanel.setBackground(Color.BLACK);	
	}
	
	public void showAllCards(Object activePlayers) {
		if (activePlayers instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<Player> players = (List<Player>) activePlayers;
			playersCardsList = new ArrayList<>();
			
			cardsToComparePanel = new JPanel();
			System.out.println("ACTIVE PLAYERS: " + players);
			int j = 1;
			for (Player player : players) {
				String name = player.getName();
				String playerCard = player.getCard(0).getPattern();
				ImageInformation cardInfo = createCardImage(playerCard);
				JPanel panelToShow = createCardPanel(name, cardInfo);
				
				playersCardsList.add(panelToShow);
				cardsToComparePanel.add(panelToShow);
				
				for (int i = 0; i < j; j--) {
					createCardsPanel(cardsToComparePanel, players.size(), cardInfo.getWidth(), cardInfo.getHeight());
				}
			}
			
			this.shadowPane.addCount();

			escMenu.setIsCardAction(true);

			layeredPane.add(cardsToComparePanel, JLayeredPane.POPUP_LAYER);
			updatePane();
		}
	}
	
	public void showWinnerCard(Object playersIndexes) {
		if (playersIndexes instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List <Integer> indexes = (List<Integer>) playersIndexes;
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: " + indexes);
			//: Cannot invoke "java.util.List.get(int)" because "this.playersCardsList" is null
			//problem je ze som predtym nevytboril this.playersCardsList treba to pozriet pri baronovi este raz
			for (Integer index : indexes) {
				playersCardsList.get(index).getComponent(0).setBackground(Color.RED);
			}
			setTimer(cardsToComparePanel, 2500);
		}
	}
	
	public void updateWinner(Object winner) {
		if (winner instanceof Player) {
			Player player = (Player) winner;
			playersPanel.get(player.getIndex()).updatePlayerScore(player.getScore());
			updatePane();
		} else {
	        System.out.println("Invalid input: winner is not instanceof Player");
	    }
	}
	
	public void createResultPanel(Object winString) {
		if (winString instanceof String) {
			String winStr = (String) winString;
			JLabel label = CustomTextFont.createFont(winStr);
			JPanel panel = new CenterPanelForLabel(label, 500, 30);
			panel.setBounds(gameWidth / 2 - 250, gameHeight / 2 - 15, 500, 30);
			
			this.shadowPane.addCount();
			escMenu.setIsCardAction(true);
			
			layeredPane.add(panel, JLayeredPane.POPUP_LAYER);	
			setTimer(panel, 2500);
		}
	}
	
	public void guessCardGuardAction() {
		String[] cardNames = new String[] {"Priest", "Baron", "Handmaid", "Prince", "King", "Countess", "Princess"};
		for (int i = 0; i < cardNames.length; i++) {
			
			String cardName = cardNames[i];	
			ImageIcon image = new ImageIcon(getClass().getResource("/guessingCards/" + cardNames[i] + ".png"));
			JLabel labelCard = new JLabel(image);
			
			labelCard.setBounds(i * image.getIconWidth(), 0, image.getIconWidth(), image.getIconHeight());
			labelCard.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					controller.choosedCardByGuard(cardName);
					GameGui.this.shadowPane.decreaseCount();
					layeredPane.remove(guardGuessPanel);
					escMenu.setIsCardAction(false);
					guardGuessPanel = null;
					updatePane();
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					labelCard.setBorder(BorderFactory.createLineBorder(Color.RED));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					labelCard.setBorder(null);
				}
				});

			if (i == 0) {
				this.guardGuessPanel = new JPanel(null);
				int xPosition = (gameWidth / 2) - (image.getIconWidth() * 7 / 2);
				int yPosition = (gameHeight / 2) - (image.getIconHeight() / 2);
				this.guardGuessPanel.setBounds(xPosition, yPosition, image.getIconWidth()* 7, image.getIconHeight());
				this.guardGuessPanel.setBackground(Color.BLACK);
			}
			
			this.guardGuessPanel.add(labelCard);
		}

		this.shadowPane.addCount();
		escMenu.setIsCardAction(true);
		
		layeredPane.add(guardGuessPanel, JLayeredPane.POPUP_LAYER);
		updatePane();
	}
	
	public void guessCardPriestAction(Player targetPlayer) {
		String playerName = targetPlayer.getName();
		String playerCard = targetPlayer.getCard(0).getPattern();	
		ImageInformation cardInfo = createCardImage(playerCard);
		
		JPanel targetPanel = createCardPanel(playerName, cardInfo);
	
		int cardWidth = cardInfo.getWidth();
		int cardHeight = cardInfo.getHeight();
		int xPosition = gameWidth / 2 - cardInfo.getWidth() / 2;
		int yPosition = gameHeight / 2 - cardHeight / 2;
		int labelHeight = 25;
		
		this.shadowPane.addCount();
		escMenu.setIsCardAction(true);
		
		targetPanel.setBounds(xPosition, yPosition, cardWidth, cardHeight + labelHeight);
		layeredPane.add(targetPanel, JLayeredPane.POPUP_LAYER);
		setTimer(targetPanel, 2000);
	}
	
	public void guessCardBaronAction(Player targetPlayer) {
		int choosedCardRank = 3;
		createTemporaryPanelInCenter(targetPlayer, choosedCardRank);
	}
	
	public void cardPrinceAction(Player targetPlayer) {
		if (targetPlayer.getName().equals(activePlayerName)) {
			this.activePlayerCardsPanel.removeAll();
			this.activePlayerCardsPanel.revalidate();
			this.activePlayerCardsPanel.repaint();
			
			Timer timer = new Timer(500, e -> {
					String newCard = targetPlayer.getCard(1).getPattern();
					createJLabelForCard(newCard, 0, 0);
					this.activePlayerCardsPanel.revalidate();
					this.activePlayerCardsPanel.repaint();
			});
			timer.setRepeats(false);
			timer.start();
		}
		
		for (PlayerGui playerGui : playersPanel) {
			if(playerGui.getPlayerName().equals(targetPlayer.getName())) {
				playerGui.setIsFirstCard();
				playerGui.addBackCard();
			}
		}
	}
	
	public void cardKingAction(Player targetPlayer) {
		int choosedCardRank = 6;
		createTemporaryPanelInCenter(targetPlayer, choosedCardRank);
	}
	
	private JPanel createCardPanel(String playerName, ImageInformation cardInfo) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.BLACK);
		
		JLabel nameLabel = CustomTextFont.createFont(playerName);
		JLabel cardImage = cardInfo.getLabel();
		
		int cardWidth = cardInfo.getWidth();
		int labelHeight = 25;
		
		JPanel namePanel = new CenterPanelForLabel(nameLabel, cardWidth, labelHeight);
		
		panel.add(namePanel);
		panel.add(cardImage);
		return panel;
	}
	
	private void createTemporaryPanelInCenter(Player targetPlayer, int choosedCardRank) {
		String targetName = targetPlayer.getName();
		String targetPlayerCard = targetPlayer.getCard(0).getPattern();
		ImageInformation targetCardInfo = createCardImage(targetPlayerCard);
		JPanel targetPanelToShow = createCardPanel(targetName, targetCardInfo);
	
		String myCardName = card1Rank == choosedCardRank ? card2Name : card1Name;
		ImageInformation myCardInfo = createCardImage(myCardName);	
		JPanel myPanelToShow = createCardPanel(activePlayerName, myCardInfo);
		
		cardsToComparePanel = new JPanel();
		createCardsPanel(cardsToComparePanel, 2, myCardInfo.getWidth(), myCardInfo .getHeight());
		cardsToComparePanel.add(myPanelToShow);
		cardsToComparePanel.add(targetPanelToShow);
		
		playersCardsList = new ArrayList<>();
		playersCardsList.add(myPanelToShow);
		playersCardsList.add(targetPanelToShow);
		
		this.shadowPane.addCount();
		escMenu.setIsCardAction(true);

		layeredPane.add(cardsToComparePanel, JLayeredPane.POPUP_LAYER);
		updatePane();
		if (choosedCardRank == 6) {
			Timer timer = new Timer(500, e -> {
				Icon tempIcon = targetCardInfo.getLabel().getIcon();
				targetCardInfo.getLabel().setIcon(myCardInfo.getLabel().getIcon());
				myCardInfo.getLabel().setIcon(tempIcon);
				updatePane();
				setTimer(cardsToComparePanel, 2000);
			});
			timer.setRepeats(false);
			timer.start();
		}
	}
	
	private ImageInformation createCardImage(String cardName) {
		ImageIcon icon = new ImageIcon(getClass().getResource("/bigCards/" + cardName + ".png"));
		JLabel cardImage = new JLabel(icon);			
		cardImage.setAlignmentX(Component.CENTER_ALIGNMENT); 
		cardImage.setMaximumSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		cardImage.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		return new ImageInformation(cardImage, icon.getIconWidth(), icon.getIconHeight());
	}
	
	private void setTimer(JPanel panel, int time) {
		Timer timer = new Timer(time, e -> {
			if (!escMenu.getIsMenuOpened()) {
				this.shadowPane.decreaseCount();
				updatePane();
			}
			escMenu.setIsCardAction(false);
			layeredPane.remove(panel);
			updatePane();
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	public void removeLoserCard(Player loserPlayer) {
		for (PlayerGui panel : playersPanel) {
			if (loserPlayer.getName().equals(panel.getPlayerName())) {
				this.activePlayerCardsPanel.removeAll();
				updatePane();
				break;
			}
		}
	}

	public void clearBoard() {
		for (PlayerGui player : playersPanel) {
			player.removeAllBackCards();
			player.removeAllPlayedCards();
		}
		playersBottomPanel.removeAll();
	}
	
	public void playersByScore(Object playersByScore) {
		if (playersByScore instanceof List<?>) {
			scoreboardPanel = new JPanel();
			scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
			scoreboardPanel.setBounds(875, 410, 190, 150);
			scoreboardPanel.setOpaque(false);
			scoreboardImage = createImageLabel("resultBcg", 0, 0, true);
			int rank = 1;
			
			this.shadowPane.addCount();
			this.layeredPane.add(scoreboardImage, JLayeredPane.POPUP_LAYER);
			this.layeredPane.add(scoreboardPanel, JLayeredPane.DRAG_LAYER);
			
			@SuppressWarnings("unchecked")
			List<Player> players = (List<Player>) playersByScore;
			
			for (Player actualPlayer : players) {
				JLabel playerRank = CustomTextFont.createFont(rank + ". " + actualPlayer.getName());
				scoreboardPanel.add(playerRank);
				rank++;
			}
			escMenu.addComponentToHide(scoreboardImage);
			escMenu.addComponentToHide(scoreboardPanel);
			
			updatePane();
		}
	}
	
	private void removeScoreboard() {
		if (scoreboardPanel != null) {
			this.layeredPane.remove(scoreboardImage);
			this.layeredPane.remove(scoreboardPanel);
			this.escMenu.clearComponentToHide();
		}
	}
	
	private void updatePane() {
		this.layeredPane.revalidate();
		this.layeredPane.repaint();
	}
	
	public ShadowPane getShadowPane() {
		return this.shadowPane;
	}
	
	private void showZoomedCard(JLabel labelCard, String cardName) {
		labelCard.addMouseListener(new MouseAdapter() {
			JLabel zoomedCardImage;
			@Override
			public void mouseEntered(MouseEvent e) {
				ImageIcon imageBig = new ImageIcon(getClass().getResource("/bigCards/" + cardName + ".png"));
				int imageWidth = imageBig.getIconWidth();
				int imageHeight = imageBig.getIconHeight();
				zoomedCardImage = new JLabel(imageBig);
				zoomedCardImage.setBounds(gameWidth / 2 - imageWidth / 2, gameHeight / 2 - imageHeight / 2, imageWidth, imageHeight);
				layeredPane.add(zoomedCardImage, JLayeredPane.MODAL_LAYER);
				updatePane();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				layeredPane.remove(zoomedCardImage);
				updatePane();
			}
		});
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JLayeredPane getLayeredPane() {
		return layeredPane;
	}

}
