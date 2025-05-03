package loveletter;
public class KingCard extends Card {
	
	KingCard(String pattern, Integer rank, Integer quantity) {
		super(pattern, rank, quantity);
    }

	@Override
	public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) {
		Card chosenPlayerCard = chosenPlayer.getCard(0);
		int mySecondCardIndex = myCardToCompare(chosenCardIndex, player);
		Card myCard = player.getCard(mySecondCardIndex);
		game.triggerCardAction("KING", chosenPlayer);
		changeCards(chosenPlayer, player, chosenPlayerCard, myCard, mySecondCardIndex);
		if (chosenCardIndex == 1) {
			game.changeChosenCardIndex(0);
		}
		return null;
	}
	
	private int myCardToCompare(int chosenCardIndex, Player player) {
		return (chosenCardIndex == 0) ?  1 : 0;
	}
	
	private void changeCards(Player chosenPlayer, Player player, Card chosenPlayerCard, Card myCard, int mySecondCardIndex) {
		Card tempCardStore = chosenPlayerCard;
		chosenPlayer.removeCard(0);
		chosenPlayer.addCard(myCard);
		player.removeCard(mySecondCardIndex);
		player.addCard(tempCardStore);
	}
}
