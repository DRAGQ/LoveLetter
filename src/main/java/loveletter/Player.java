package loveletter;
import java.util.ArrayList;
import java.util.List;

public class Player {
	
	private String name;
	private int playerIndex, score;
	private boolean lostRound, isProtected;
	private List<Card> myCards, playedCards;
	
	Player(String name, int index) {	
		this.name = name;
		this.playerIndex = index;
		this.score = 0;
		this.lostRound = false;
		this.isProtected = false;
		this.myCards = new ArrayList<>();
		this.playedCards = new ArrayList<>();
	}
	
	public int getIndex() {
		return playerIndex;
	}
	
	public void addCard(Card card) {
		myCards.add(card);
	}

	public void removeCard(int index) {
		myCards.remove(index);
	}
	
	public Card getCard(int index) {
		return myCards.get(index);
	}
	
	public int getScore() {
		return score;
	}
	
	public void updateScore() {
		score += 1;
	}
	
	public void resetScore() {
		score = 0;
	}
	
	public boolean getLostRound() {
		return this.lostRound;
	}
	
	public void setLostRound() {
		this.lostRound = true;
		for (Card card : myCards) {
			if (!playedCards.contains(card)) {
				addPlayedCard(card);
			}
		}
		myCards.removeAll(myCards);
	}
	
	public void deactivateLostRound() {
		this.lostRound = false;
	}
	
	public void activateProtection() {
		isProtected = true;
	}
	
	public void deactivateProtection() {
		isProtected = false;
	}
	
	public boolean isProtected() {
		return isProtected;
	}
	
	public String getName() {
		return name;
	}
	
	public synchronized String getLastPlayedCard() {
		return playedCards.get(playedCards.size() -1).getPattern();
	}
	
	public synchronized int getSizePlayedCards() {
		return playedCards.size();
	}
	
	public int getValueOfPlayedCards() {
		int value = 0;
		for (Card card : playedCards) {
			value += card.getRank();
		}
		return value;
	}
	
	public synchronized void addPlayedCard(Card usedCard) {
		playedCards.add(usedCard);
		notifyAll();
	}
	
	public void clearPlayer() {
		deactivateProtection();
		myCards.clear();
		playedCards.clear();
		deactivateLostRound();
	}
	
}
