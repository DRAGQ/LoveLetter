package loveletter;
public class HandmaidCard extends Card{
	HandmaidCard(String pattern, Integer rank, Integer quantity) {
		super(pattern, rank, quantity);
    }

	@Override
	public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) { 
		//Protect one round
		player.activateProtection();
		return null;
	}

}
