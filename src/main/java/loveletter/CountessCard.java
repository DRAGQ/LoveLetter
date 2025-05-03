package loveletter;
public class CountessCard extends Card{
	
	CountessCard(String pattern, Integer rank, Integer quantity) {
		super(pattern, rank, quantity);
    }

	@Override
	public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) {
		//If I have prince or king in hand I have to discard this first
		return null;
	}
}
