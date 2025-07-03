package loveletter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextField;
import javax.swing.SwingWorker;



public class Game {

	private Deck deck;
	private List<GameEventListener> listeners;
	private List<CardActionListener> actionListeners;
	private List<Player> players, activePlayers;
	//private List<Card> cards;
	private ArrayList<Integer> availablePlayersForFirstCard, availablePlayersForSecondCard;
	private GameWorker gameWorker;
	private Iterator<Player> iterator;
	private Player chosenPlayer, lastWonPlayer;
	private final Lock lock;
	private final Condition playerTurn, playerIsChoosingCardWithGuard;
	private boolean gameIsRunning, playerMadeMove, choosedCardByGuard;
	private int roundNumber, numberOfPlayers, requiredScore, chosenCardIndex;
	private String ChoosedCard;
	
	
	Game(Deck deck) {
		this.listeners = new ArrayList<>();
		this.actionListeners = new ArrayList<>();
		this.deck = deck;
		lock = new ReentrantLock();
		playerTurn = lock.newCondition();
		playerIsChoosingCardWithGuard = lock.newCondition();
	}
	
	private void newPlayersLists() {
		this.players = new ArrayList<>();
		this.activePlayers = new ArrayList<>(this.players);
	}
	
	public void startGame(int numberOfPlayers, ArrayList<JTextField> playerNamesField) {
		newPlayersLists();
		this.numberOfPlayers = numberOfPlayers;
		this.lastWonPlayer = null;
		this.roundNumber = 0;
		if (numberOfPlayers == 2) {
			//this.numberOfGames = 7;
			this.requiredScore = 1;
		} else if (numberOfPlayers == 3) {
			this.requiredScore = 5;
		} else {
			this.requiredScore = 4;
		}
		createPlayerObjects(playerNamesField);
		roundSetUp();
	}
	
	public void resetGame(ArrayList<JTextField> playerNamesField) {
		resetPlayersScore();
		this.roundNumber = 0;
		if (gameWorker != null && !gameWorker.isDone()) {
			Game.this.gameIsRunning = false;
		    gameWorker.cancel(true);
		}
		else {
			startGame(this.numberOfPlayers, playerNamesField);
		}
	}
	
	private void resetPlayersScore() {
		for (Player player : players) {
			player.resetScore();
		}
	}
	
	public void roundSetUp() {
		System.out.println("Round SET UP!");
		this.roundNumber++;
		clearPlayers();
		
		this.deck.clearDeck();
		this.deck.createPackOfCards();
		//this.cards = this.deck.getCards();
		
		this.playerMadeMove = false;
		this.choosedCardByGuard = false;
		
		triggerEvent("CLEAR_BOARD", true);
		triggerEvent("NEW_ROUND", this.roundNumber);

		this.deck.shuffle();
		this.deck.setSecretCard(this.deck.dealCard());
		triggerEvent("NEW_SECRET_CARD", true);
		
		if (this.numberOfPlayers == 2) {
			this.deck.discardFirstThreeCards();
			triggerEvent("FIRST_THREE_CARDS", this.deck.getFirstThreeCards());
		}
		dealOneCardToPlayers();
		renderBackCardForEach();
		this.activePlayers.clear();
		this.activePlayers.addAll(this.players);
		this.iterator = this.activePlayers.iterator();

		System.out.println("SECRET CARD IS: " + this.deck.getSecretCard());
		this.gameIsRunning = true;
		triggerEvent("NUMBER_OF_CARDS", deck.getNumberOfCards());
		
		round();
	}
	public void round() {
		if (gameWorker != null && !gameWorker.isDone()) {
		    gameWorker.cancel(true);
		}
		gameWorker = new GameWorker();
		gameWorker.execute();
		
	}
	
	
	private class GameWorker extends SwingWorker<Void, GameEvent> {
		
		 @Override
		 protected Void doInBackground() throws Exception {
			 	int firstCardIndex = 0;
				int secondCardIndex = 1;
				
				 while (Game.this.gameIsRunning && !isCancelled()) {
					if (!Game.this.iterator.hasNext()) {
						Game.this.iterator = Game.this.activePlayers.iterator();
					}

					Player player = Game.this.iterator.next();

					if (Game.this.lastWonPlayer != null && player != lastWonPlayer) {
						continue;
					}

					Game.this.lastWonPlayer = null;
					if (player.getLostRound()) {
						Game.this.iterator.remove();
						continue;
					}

					if (player.isProtected()) {
						player.deactivateProtection();
					}
					
					Game.this.addCardToPlayer(player);
					
					setupAvailablePlayersForEachCard(player, firstCardIndex, secondCardIndex);		
					//returnAvailablePlayers(availablePlayersForFirstCard, player, activePlayers, firstCardIndex);
					//returnAvailablePlayers(availablePlayersForSecondCard, player, activePlayers, secondCardIndex);
					
					//publish(new GameEvent("All_ACTIVE_PLAYERS", availablePlayersForFirstCard));
					//publish(new GameEvent("All_ACTIVE_PLAYERS", availablePlayersForSecondCard));
					
					Thread.sleep(400);
					renderCards(player.getIndex()); //render 2 cards to active player
					renderBackCard(player.getIndex()); //render 1 back of card
					
					Thread.sleep(400);
					
					publish(new GameEvent("NUMBER_OF_CARDS", Game.this.deck.getNumberOfCards()));

					lock.lock();
					try {
						while (!Game.this.playerMadeMove && !isCancelled()) {
							playerTurn.await(); //Choosing card and player
						}
						Game.this.playerMadeMove = false;
					} finally {
						lock.unlock();
					}
					
					Card chosenCard = player.getCard(Game.this.chosenCardIndex);
					player.addPlayedCard(chosenCard);
					Thread.sleep(100);

					triggerEvent("PLAYED_CARD", player);

					ArrayList<Integer> availablePlayersIndexes;
					availablePlayersIndexes = (chosenCardIndex == 0 ? availablePlayersForFirstCard : availablePlayersForSecondCard);
					
					Player isLooser = null;
					
					if (availablePlayersIndexes.size() >= 1 && (Game.this.players.get(availablePlayersIndexes.get(0)) != player || chosenCard.getRank() == 4 || chosenCard.getRank() == 5)) {
						isLooser = chosenCard.cardAction(Game.this.chosenCardIndex, player, Game.this.chosenPlayer, Game.this.deck, Game.this);
					}
					
					publish(new GameEvent("NUMBER_OF_CARDS", Game.this.deck.getNumberOfCards()));
					Thread.sleep(2000);
					
					if (isLooser != null) {
						isLooser.setLostRound();
						triggerEvent("PLAYED_CARD", isLooser);
						Thread.sleep(1300);
					}

					if (!player.getLostRound()) {
						player.removeCard(chosenCardIndex);
					}
					
					if (checkWinner(Game.this.activePlayers) != null) {
						System.out.println("Only 1 person left");
						gameIsRunning= false;
					}
					else if (isDeckEmpty(Game.this.deck.getNumberOfCards())) {
						checkWinnerByHighestCard(Game.this.activePlayers);
						gameIsRunning= false;
					}
					else {
						clearPreviousPlayerGui(null);
						Thread.sleep(200);
					}
				 }
			return null;
			}
		 	@Override
	        protected void process(List<GameEvent> events) {
	            for (GameEvent event : events) {
	            	for (GameEventListener listener : listeners) {
	        			listener.onGameEvent(event);
	        		}
	            }
	        }
	        
	        @Override
	        protected void done() {
	        	System.out.println("DONE IS CALLED");
	        	boolean isEnd = false;
	        	for (Player player : activePlayers) {
	        		if (Game.this.requiredScore == player.getScore()) {
						results();
						isEnd = true;
						break;
					}
	        	}

				if (!isEnd) {
					clearPreviousPlayerGui(null);
					Game.this.roundSetUp();
				}
	        }
	}
	
	public void addGameEventListener(GameEventListener listener) {
		listeners.add(listener);
	}
	
	public void addGameCardActionListener(CardActionListener listener) {
		actionListeners.add(listener);
	}
	
	public void triggerEvent(String eventType, Object eventData) {
		System.out.println("EVENT DATA IS: "+eventData);
		GameEvent event = new GameEvent(eventType, eventData);
		for (GameEventListener listener : listeners) {
			listener.onGameEvent(event);
		}
	}
	
	public void triggerCardAction(String actionCard, Player targetPlayer) {
		System.out.println("TRIGGER CARD ACTION: " + actionCard);
		CardAction action = new CardAction(actionCard, targetPlayer);
		for (CardActionListener listener : actionListeners) {
			listener.onCardAction(action);
			
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private void createPlayerObjects(ArrayList<JTextField> playerNamesField) {
		for (int i = 0; i < this.numberOfPlayers; i++) {
			Player player = new Player(playerNamesField.get(i).getText(), i);
			this.players.add(player);
			System.out.println(this.players.get(i).getName());
		}
	}
	
	private void setupAvailablePlayersForEachCard(Player player, int firstCardIndex, int secondCardIndex) {
		availablePlayersForFirstCard  = new ArrayList<>();
		availablePlayersForSecondCard  = new ArrayList<>();
		availablePlayersForFirstCard.removeAll(availablePlayersForFirstCard);
		availablePlayersForSecondCard.removeAll(availablePlayersForSecondCard);
		
		returnAvailablePlayers(availablePlayersForFirstCard, player, activePlayers, firstCardIndex);
		returnAvailablePlayers(availablePlayersForSecondCard, player, activePlayers, secondCardIndex);
		
		triggerEvent("All_ACTIVE_PLAYERS", availablePlayersForFirstCard);
		triggerEvent("All_ACTIVE_PLAYERS", availablePlayersForSecondCard);
	}
	
	public int getPlayerScore(String playerName) {
		for (Player player: players) {
			if (playerName.equals(player.getName())) {
				return player.getScore();
			}
		}
		
		return 0;
	}
	
	private void clearPlayers() {
		for (Player player : this.players) {
			player.clearPlayer();
		}
	}
	
	private void dealOneCardToPlayers() {
		System.out.println("444yt: " + this.players.size());
		for (Player player : this.players) {
			player.addCard(this.deck.dealCard());
		}
	}
	
	public void addCardToPlayer(Player player) {
		player.addCard(deck.dealCard());
	}
	
	public void secretCardIsUsed() {
		triggerEvent("SECRET_CARD_USED", true);
	}
	
	public void renderBackCardForEach() {
		for (Player player : this.players) {
			renderBackCard(player.getIndex());
		}
	}
	
	public void renderBackCard(int index) {
		triggerEvent("BACK_SITE_OF_CARD", this.players.get(index));
	}
	
	public void renderCards(int index) {
		triggerEvent("PLAYER_CARDS", this.players.get(index));
	}
	
	public void clearPreviousPlayerGui(Void Void) {
		triggerEvent("CLEAR_PLAYER_DECK", Void);
	}
	
	public void chosenPlayerAndCard(String choosedPlayerName, int currentPlayerIndex, int cardIndex) {
		this.chosenCardIndex = cardIndex;
		for (Player player : activePlayers) {
			if (player.getName().equals(choosedPlayerName)) {
				this.chosenPlayer = player;
			}
		}
		lock.lock();
		try {
			this.playerMadeMove = true;
			this.playerTurn.signal();
		} finally {
			lock.unlock();
		}
	}
	

	private void returnAvailablePlayers(ArrayList<Integer> availablePlayersIndexes, Player player, List<Player> activePlayers, int cardIndex) {
		ArrayList<Integer> unprotectedPlayersIndexes = new ArrayList<>();
		System.out.println("SIZE IS: " + activePlayers.size());
		for(int i = 0; i < activePlayers.size(); i++) {
			if (activePlayers.get(i).getIndex() != player.getIndex() && !activePlayers.get(i).isProtected() && !activePlayers.get(i).getLostRound()) {
				unprotectedPlayersIndexes.add(activePlayers.get(i).getIndex());
			}
		}
		int chosenCardRank = player.getCard(cardIndex).getRank();
		if (chosenCardRank == 8) {}
		else if ((chosenCardRank == 5 || chosenCardRank == 6) && (player.getCard(0).getRank() == 7 || player.getCard(1).getRank() == 7)) {}
		else if (chosenCardRank == 4 || chosenCardRank == 7 || unprotectedPlayersIndexes.isEmpty()) {
			availablePlayersIndexes.add(player.getIndex());
		} 
		else {
			if (chosenCardRank == 5) {
				availablePlayersIndexes.add(player.getIndex());
			}
			availablePlayersIndexes.addAll(unprotectedPlayersIndexes);

			for (int i = 0; i < availablePlayersIndexes.size(); i++) {
			System.out.println("ALL available players are:");
			System.out.println(this.players.get(availablePlayersIndexes.get(i)).getName());
			}
		}	
	}
	
	
	public String guessCardGuardAction(String actionCard, Player targetPlayer) throws InterruptedException {
		triggerCardAction(actionCard, targetPlayer);
		lock.lock();
		try {
			while (!this.choosedCardByGuard) {
				this.playerIsChoosingCardWithGuard.await();
			}
			this.choosedCardByGuard = false;
		} finally {
			lock.unlock();
			System.out.println("UNLOCKED");
		}
		return this.ChoosedCard;
		
	}
	public void choosedCardByGuard(String pattern) {
		lock.lock();
		try {
			ChoosedCard = pattern;
			this.choosedCardByGuard = true;
			this.playerIsChoosingCardWithGuard.signal();
		} finally {
			lock.unlock();
			System.out.println("UNLOCKED");
		}
	}
	
	private Player checkWinner(List<Player> activePlayers) {
		int counter = 0;
		Player winner = null;
		for (int i = 0; i < activePlayers.size(); i++) {
			if (!activePlayers.get(i).getLostRound()) {
				counter++;
				winner = activePlayers.get(i);
			}
		}
		if (counter == 1) {
			this.lastWonPlayer = winner;
			winner.updateScore();
			
			triggerEvent("WINNER_OF_ROUND", winner.getName() + " IS WINNER !");
			setTimer(3000);
			triggerEvent("UPDATE_WINNER", winner);
			
			System.out.println("Player: " + winner.getName() + "is WINNER!!!!" + "His score is: " + winner.getScore());
			return winner;
		}
		return null;
	}
	
	private boolean isDeckEmpty(int numberOfCards) {
		if (numberOfCards == 0) {
			return true;
		}
		return false;
	}
	private void checkWinnerByHighestCard(List<Player> activePlayers) {
		List<Player> lastStandPlayers = new ArrayList<>();
		
		for (Player player : activePlayers) {
			if (!player.getLostRound()) {
				lastStandPlayers.add(player);
			}
		}
		triggerEvent("SHOW_ALL_CARDS", lastStandPlayers);	
		setTimer(1000);
		
		Player winner = null;
		List<Player> DrawPlayers = new ArrayList<>();
		List<Integer> winnersIndexes = new ArrayList<>();
		int rank = 0;
		int numberOfCards = 0;
		
		for (Player player : lastStandPlayers) {
			int playerCardRank = player.getCard(0).getRank();
			if (playerCardRank > rank) {
				rank = playerCardRank;
				winner = player;
			}
		}
		for (int i = 0; i < lastStandPlayers.size(); i++) {
			Player player = lastStandPlayers.get(i);
			int playerCardRank = player.getCard(0).getRank();
			if (playerCardRank == rank) {
				winnersIndexes.add(i);
				DrawPlayers.add(player);
				numberOfCards++;
			}
		}

		//show highest card/s.
		triggerEvent("SHOW_WINNER_CARD",  winnersIndexes);
		setTimer(3000);
		
		/*if (numberOfCards == 1) {
			winner.updateScore();
			triggerEvent("WINNER_OF_ROUND", winner.getName() + " IS WINNER !");
			setTimer(3000);
			
			triggerEvent("UPDATE_WINNER", winner);*/
			System.out.println("Player: " + winner.getName() + "is Winner!" + "With Card rank: " + rank + "and score: " + winner.getScore());
		//} else {
			if (numberOfCards != 1) {
			//
			for (Player player : DrawPlayers) {
				if (winner.getValueOfPlayedCards() < player.getValueOfPlayedCards()) {
					winner = player;
				}
			}
			/*winner.updateScore();
			triggerEvent("UPDATE_WINNER", winner);
			// to draw vymazat a miesto toho poslem winnera
			triggerEvent("WINNER_OF_ROUND", " IS WINNER !");
			setTimer(3000);*/
		}
			winner.updateScore();
			triggerEvent("WINNER_OF_ROUND", winner.getName() + " IS WINNER !");
			setTimer(3000);
			
			triggerEvent("UPDATE_WINNER", winner);
			lastWonPlayer = winner;
	}
	
	private void setTimer(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*private void showWinnerCards(List<Integer> winnersIndexes) {
		triggerEvent("SHOW_WINNER_CARD",  winnersIndexes);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}*/
	
	private void results() {
		 List<Player> playersByScore = new ArrayList<>();
		 Player tempPlayer = this.players.get(0);
		
		for (int i = 0; i < this.numberOfPlayers; i++) {
			for (Player player : this.players) {
				if (tempPlayer.getScore() < player.getScore()) {
					tempPlayer = player;
				}
			}
			playersByScore.add(tempPlayer);
			this.players.remove(tempPlayer);
			if (this.players.size() > 0) {
				tempPlayer = this.players.get(0);
			}
		}
		triggerEvent("PLAYERS_RESULTS", playersByScore);
	}
	
	public void changeChosenCardIndex(int index) {
		this.chosenCardIndex = index;
	}
	
	public Deck getDeck() {
        return deck;
    }
	
}
