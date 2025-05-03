package loveletter;
public class GuardCard extends Card {

	private String chosenPlayerCard;
	String guessedCard;
	
		GuardCard(String pattern, Integer rank, Integer quantity) {
        super(pattern, rank, quantity); 
    }

		@Override
		public Player cardAction(int chosenCardIndex, Player player, Player chosenPlayer, Deck deck, Game game) {
			chosenPlayerCard = chosenPlayer.getCard(0).getPattern();
			
			String guessedCard = null;
			try {
				guessedCard = game.guessCardGuardAction("GUARD", chosenPlayer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return comparePlayerGuessWithOponentCard(chosenPlayerCard, guessedCard, chosenPlayer);
		}
	
	private Player comparePlayerGuessWithOponentCard(String oponentCard, String guessedCard, Player chosenPlayer) {
		if (oponentCard.equals(guessedCard)) {
			System.out.println("You guessed it!");
			return chosenPlayer;
		}
		else {
			System.out.println("You don't guessed it.");
			return null;
		}
	}
}