package loveletter;
import java.util.ArrayList;
import java.util.List;

public class BaronCard extends Card{
	Game game;
	List<Integer> winnerIndex;
	
	BaronCard(String pattern, Integer rank, Integer quantity) {
		super(pattern, rank, quantity);
    }

	@Override
	public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) {
		this.game = game;
		this.winnerIndex = new ArrayList<>();
		int chosenPlayerCardRank = chosenPlayer.getCard(0).getRank();
		int secondCardIndex = (chosenCardIndex == 0 ? 1 : 0);
		int myCardRank = player.getCard(secondCardIndex).getRank();
		game.triggerCardAction("BARON", chosenPlayer);
		Player looser = compareCardsAndReturnLoser(chosenCardIndex, chosenPlayerCardRank, myCardRank, chosenPlayer, player);
		game.triggerEvent("SHOW_WINNER_CARD",  winnerIndex);
		return looser;
	}
	
	private Player compareCardsAndReturnLoser(int chosenCardIndex, int chosenPlayerCardRank, int myCardRank, Player chosenPlayer, Player player) {
		if (chosenPlayerCardRank > myCardRank) {
			game.triggerCardAction("REMOVE_LOSER_CARD", chosenPlayer);
			this.winnerIndex.add(1);
			return player;
		}
		else if (chosenPlayerCardRank < myCardRank) {
			this.winnerIndex.add(0);
			return chosenPlayer;
		}
		else {
			this.winnerIndex.add(0);
			this.winnerIndex.add(1);
			return null;
		}
	}
}
