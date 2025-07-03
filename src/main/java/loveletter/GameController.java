package loveletter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GameController implements GameEventListener, CardActionListener{

	private Game game;
	private GameGui gui;
	
	public GameController(Game game) {
		this.game = game;
		//this.gui = gui;
		
		game.addGameEventListener(this);
		game.addGameCardActionListener(this);
		//gui.setController(this);
	}
	
	public void setGuiController(GameGui gui) {
		this.gui = gui;
		//gui.setController(this);
	}
	
	public void startGame(int numberOfPlayers, ArrayList<JTextField> playerNamesField) {
		game.startGame(numberOfPlayers, playerNamesField);
	}
	
	public int getPlayerScore(String playerName) {
		return game.getPlayerScore(playerName);
	}
	
	public void resetGame(ArrayList<JTextField> playerNamesField) {
		game.resetGame(playerNamesField);
	}
	
	public void chosenPlayerAndCard(String choosedPlayerName, int currentPlayerIndex, int cardIndex) {
		game.chosenPlayerAndCard(choosedPlayerName, currentPlayerIndex, cardIndex);
	}
	
	public void choosedCardByGuard(String pattern) {
		game.choosedCardByGuard(pattern);
	}
	
	
	
	@Override
	public void onGameEvent(GameEvent event) {
		switch(event.getEventType()) {
			case "NEW_ROUND":
				SwingUtilities.invokeLater(() -> {
					gui.showRoundNumber(event.getEventData());
				});
				break;
			case "NUMBER_OF_CARDS":
				SwingUtilities.invokeLater(() -> {
					gui.updateNumberOfCards(event.getEventData());
				});
				break;
			case "NEW_SECRET_CARD":
				SwingUtilities.invokeLater(() -> {
					gui.setSecretCard(event.getEventData());
				});
				break;
			case "SECRET_CARD_USED":
				SwingUtilities.invokeLater(() -> {
					gui.removeSecretCard(event.getEventData());
				});
				break;
			case "FIRST_THREE_CARDS":
				SwingUtilities.invokeLater(() -> {
					gui.showFirstThreeCards(event.getEventData());
				});
				break;
			case "BACK_SITE_OF_CARD":
				SwingUtilities.invokeLater(() -> {
					gui.updatePlayersBackSideCards(event.getEventData());
				});
				break;
			case "PLAYER_CARDS":
				SwingUtilities.invokeLater(() -> {
					gui.drawCardsGui(event.getEventData());
				});
				break;
			case "CLEAR_PLAYER_DECK":
				SwingUtilities.invokeLater(() -> {
					gui.clearPlayerDeck(event.getEventData());
				});
				break;
			case "All_ACTIVE_PLAYERS":
				try {
					SwingUtilities.invokeAndWait(() -> {
						gui.saveAllActivePlayersForEachCard(event.getEventData());
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case "PLAYED_CARD":
				try {
					SwingUtilities.invokeAndWait(() -> {
						gui.showPlayedCard(event.getEventData());
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case "CLEAR_BOARD":
				SwingUtilities.invokeLater(() -> {
					gui.clearBoard();
				});
				break;
			case "SHOW_ALL_CARDS":
			try {
				SwingUtilities.invokeAndWait(() -> {
					gui.showAllCards(event.getEventData());
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				break;
			case "UPDATE_WINNER":
				SwingUtilities.invokeLater(() -> {
					gui.updateWinner(event.getEventData());
				});
				break;
			case "SHOW_WINNER_CARD":
				SwingUtilities.invokeLater(() -> {
					gui.showWinnerCard(event.getEventData());
				});
				break;
			case "WINNER_OF_ROUND":
				SwingUtilities.invokeLater(() -> {
					gui.createResultPanel(event.getEventData());
				});
				break;
			case "PLAYERS_RESULTS":
				SwingUtilities.invokeLater(() -> {
					gui.playersByScore(event.getEventData());
				});
				break;
		}
		
	}

	@Override
	public void onCardAction(CardAction action) {
		switch(action.getActionCard()) {
		case "GUARD":
			SwingUtilities.invokeLater(() -> {
				gui.guessCardGuardAction();
			});
			break;
		case "PRIEST":
			SwingUtilities.invokeLater(() -> {
				gui.guessCardPriestAction(action.getTargetPlayer());
			});
			break;
		case "BARON":
			SwingUtilities.invokeLater(() -> {
				gui.guessCardBaronAction(action.getTargetPlayer());
			});
			break;
		case "PRINCE":
			SwingUtilities.invokeLater(() -> {
				gui.cardPrinceAction(action.getTargetPlayer());
			});
			break;
		case "KING":
			SwingUtilities.invokeLater(() -> {
				gui.cardKingAction(action.getTargetPlayer());
			});
			break;
		case "REMOVE_LOSER_CARD":
			SwingUtilities.invokeLater(() -> {
				gui.removeLoserCard(action.getTargetPlayer());
			});
		break;
		}
	}
	
}
