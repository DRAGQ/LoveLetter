package loveletter;
public class PriestCard extends Card{


	
	PriestCard(String pattern, Integer rank, Integer quantity) {
    super(pattern, rank, quantity);  
    }

	@Override                
	public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) {
		//Show oponent card.
		game.triggerCardAction("PRIEST", chosenPlayer);
		return null;
	}
	
	
		
		
}
