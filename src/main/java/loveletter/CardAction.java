package loveletter;
public class CardAction {
	private String actionCard;
	private Player targetPlayer;
	
	public CardAction(String actionCard, Player targetPlayer) {
		this.actionCard = actionCard;
		this.targetPlayer = targetPlayer;
	}
	
	public String getActionCard() {
		return this.actionCard;
	}

	public Player getTargetPlayer() {
		return this.targetPlayer;
	}
}
