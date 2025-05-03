package loveletter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	
	private List<Card> cards;
	private List<String> firstThreeCards;
	private Card secretCard;
	private String[] patterns;
	private Integer[] ranks;
	private Integer[] quantity;
	
	
	Deck() {
		
		this.cards = new ArrayList<>();
		this.firstThreeCards = new ArrayList<>();
		this.patterns = new String[] {"Guard", "Priest", "Baron", "Handmaid", "Prince", "King", "Countess", "Princess"};
		this.ranks = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8};
		this.quantity = new Integer[] {5, 2, 2, 2, 2, 1, 1, 1};
	}
	
	
	public List<Card> createPackOfCards() {
		String specificCard = "";
		for (int i = 0; i < patterns.length; i++) {
			for (int j = 0; j < quantity[i]; j++) {
				Card card = CardFactory.createCard(patterns[i], ranks[i], quantity[i]);
				specificCard = " - " + (j+1);
				card.setSpecificCard(specificCard);
				cards.add(card);
			}
		}
		return cards;
	}
	
	public void clearDeck() {
		cards.clear();
		clearFirstThreeCards();
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public void discardFirstThreeCards() {
		for (int i = 0; i < 3; i++) {
			this.firstThreeCards.add(cards.remove(i).getPattern());
		}
	}
	
	public List<String> getFirstThreeCards() {
		return this.firstThreeCards;
	}
	
	public void clearFirstThreeCards() {
		firstThreeCards.removeAll(firstThreeCards);
	}
	
	public Card dealCard() {
		if (!cards.isEmpty()) {
			Card removedCard = cards.remove(0);
			return removedCard;
		}
		return secretCard;
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	public Card getSecretCard() {
		return secretCard;
	}
	
	public void setSecretCard(Card secretCard) {
		this.secretCard = secretCard;
	}
	
	public String[] getAllPatterns() {
		return patterns;
	}
	
	public String getPattern(int index) {
		return patterns[index];
	}
	
	public int getNumberOfCards() {
		return cards.size();
	}
	
}