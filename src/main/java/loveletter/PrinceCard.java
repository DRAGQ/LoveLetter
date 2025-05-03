package loveletter;
public class PrinceCard extends Card{
	
	PrinceCard(String pattern, Integer rank, Integer quantity) {
		super(pattern, rank, quantity);
    }

	@Override
	public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) {
		int secondCardIndex = ((player == chosenPlayer) ? ((chosenCardIndex == 0) ? 1 : 0) : 0);
		boolean playerDefeated = false;
		
		if (chosenPlayer.getCard(secondCardIndex).getRank() == 8) {
			chosenPlayer.addPlayedCard(chosenPlayer.getCard(secondCardIndex));
			chosenPlayer.removeCard(secondCardIndex);
			playerDefeated = true;
		}
		
		else {
			chosenPlayer.addPlayedCard(chosenPlayer.getCard(secondCardIndex));
			chosenPlayer.removeCard(secondCardIndex);
			
			if (deck.getNumberOfCards() > 0) {
				game.addCardToPlayer(chosenPlayer);
			}
			else {
				player.addCard(deck.getSecretCard());
				deck.setSecretCard(null);
				game.secretCardIsUsed();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.triggerEvent("PLAYED_CARD", chosenPlayer);
			game.triggerCardAction("PRINCE", chosenPlayer);
		}
		if ((player == chosenPlayer) && (chosenCardIndex == 1)) {
			game.changeChosenCardIndex(0);
		}
		
		return playerDefeated ? chosenPlayer : null;
	}
}
