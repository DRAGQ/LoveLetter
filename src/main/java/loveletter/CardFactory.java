package loveletter;
public class CardFactory {

	
	public static Card createCard(String pattern, Integer rank, Integer quantity) {
		switch (pattern) {
        case "Guard":
            return new GuardCard(pattern, rank, quantity);
        case "Priest":
            return new PriestCard(pattern, rank, quantity);
        case "Baron":
            return new BaronCard(pattern, rank, quantity);
        case "Handmaid":
            return new HandmaidCard(pattern, rank, quantity);
        case "Prince":
            return new PrinceCard(pattern, rank, quantity);
        case "King":
            return new KingCard(pattern, rank, quantity);
        case "Countess":
            return new CountessCard(pattern, rank, quantity);
        case "Princess":
            return new PrincessCard(pattern, rank, quantity);
        default:
            throw new IllegalArgumentException("Unknown card type: " + pattern);
		}
	}
}
