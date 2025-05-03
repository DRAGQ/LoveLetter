package loveletter;
public class PrincessCard extends Card{
	
	PrincessCard(String pattern, Integer rank, Integer quantity) {
		super(pattern, rank, quantity);
    }

	@Override
	public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) {
		//The highest card, if this card is discarded you loose.
		return null;
	}
}
