package loveletter;
public abstract class Card {

	private String pattern;
	private Integer rank;
	private Integer quantity;
	private String specificCard;
	
	Card(String pattern, Integer rank, Integer quantity) {
		
		this.pattern = pattern;
		this.rank = rank;
		this.quantity = quantity;
		specificCard = "";
	}
	
	@Override
	public String toString() {
		return quantity + getSpecificCard() + " cards of " + pattern + " rank " + rank;
	}
	
	//Getters and setters
	public String getPattern() {
		return pattern;
	}
	
	public Integer getRank() {
		return rank;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getSpecificCard() {
		return specificCard;
	}
	
	public void setSpecificCard(String specificCard) {
		this.specificCard = specificCard;
	}

	public abstract Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game);
}
