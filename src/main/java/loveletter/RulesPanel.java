package loveletter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JFrame;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;


public class RulesPanel {
	
	public void showRules(JFrame frame) {
		JDialog dialog = new JDialog(frame, "Rules");
		dialog.setResizable(false);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds(0, 0, screenSize.width, screenSize.height);
		
		JFXPanel jfxPanel = new JFXPanel();
		dialog.add(jfxPanel);
		dialog.setVisible(true);
		
		URL introductionBorder = getClass().getResource("/backgroundImage/borderFrameForRules.png");
		
		URL guard = getClass().getResource("/cardsForRules/Guard.png");
		URL priest = getClass().getResource("/cardsForRules/Priest.png");
		URL baron = getClass().getResource("/cardsForRules/Baron.png");
		URL handmaid = getClass().getResource("/cardsForRules/Handmaid.png");
		URL prince = getClass().getResource("/cardsForRules/Prince.png");
		URL king = getClass().getResource("/cardsForRules/King.png");
		URL countess = getClass().getResource("/cardsForRules/Countess.png");
		URL princess = getClass().getResource("/cardsForRules/Princess.png");
				
		 Platform.runLater(() -> {
			String cssPath = getClass().getResource("/styles.css").toExternalForm();
            WebView webView = new WebView();
            String html = """
            		<html>
	            		<head>  
					        <link rel="stylesheet" type="text/css" href="%s">
					    </head>
            			<body>
	            			<div class="container">
		            			<div class="header">
		            		    	<img src=%s>
		            		    	<div>
									  	<h1>Love</h1>
									  	<h1>Letter</h1>
									</div>
		            			</div>
		            			<div class="box">
			            			<h2>CONTENTS</h2>
	            		      		<ul><b>
										  <li>16 Character Cards</li>
									</b></ul> 
								</div>
								<div class="box">
			            			<h2>SETUP</h2>
			            			<p><b>
	            		      			At first cards all 16 cards are suffled. If you are playing a 2-player game, three cards are removed from deck and placed
	            		      			to side, face up. They will not be used during this round, but are available for all players to examine during the game.
	            		      			One card is removed and placed aside face down without looking at it.<br />
	            		      			Each player draws 1 card from the deck. This is the player’s hand, and is kept secret from the other players.
									</b></p> 
									<div class="listContainer">
				            			<ul><b>
											<li>Guard (5 copies)</li>
											<li>Priest (5 copies)</li>
											<li>Baron (5 copies)</li>
											<li>Handmaid (5 copies)</li>
										</b></ul>
										<ul><b>
											<li>Prince (5 copies)</li>
											<li>King (5 copies)</li>
											<li>Countess (5 copies)</li>
											<li>Princess (5 copies)</li>
										</b></ul>
									</div>
								</div>
								<div class="box">
			            			<h2>HOW TO PLAY</h2>
			            			<p><b>
	            		      			Love Letter is played in a series of rounds. Each round represents one day. At the end of each round, one player’s letter
										reaches Princess Annette, and she reads it. When she reads enough letters from one suitor, she becomes enamored and
										grants that suitor permission to court her. That player wins the Princess’ heart and the game. 
									</b></p>
									<hr>
									<h3>-Taking a turn</h3>
									<p><b>
	            		      			During your turn, you will have a choice of two cards, 
	            		      			choose one of them and drag it to one of the players to activate the effect of this card.
	            		      			If the player cannot be chosen due to another card effect (Handmaid), your card is discarded without effect.
	            		      		</b></p>
	            		      		<p><b>	
	            		      			You must activate the effect even if it is bad for you.
	            		      			All discarded cards remain in front of the player who discarded them. Cards are overlaps so that it’s clear in which
	            		      		</b></p>
	            		      		<p><b>	
            		    		    	order they were discarded. This helps players to figure out which cards other players might be holding.
            		    		    	Once you finish applying the card’s effect, the turn passes to the next player.
									</b></p>
									<h3>-Out of the round</h3>
									<p><b>
	            		      			If a player is knocked out of the round, that player discards the card in his or her hand face up (but does not apply the
            		    		    	card’s effect) and takes no more turns until the next round.
	            		      		</b></p>
	            		      		<h3>-Played & Discarded Cards</h3>
									<p><b>
	            		      			It is important that everyone know which cards have already been played and which cards are
            		    		    	left in the deck, so any played or discarded cards must always be visible to everyone.
	            		      		</b></p>
	            		      		<h3>-End of a round</h3>
									<p><b>
	            		      			A round ends if the deck is empty at the end of a player’s turn. The royal residence closes for the evening, the person
										closest to the Princess delivers the love letter, and Princess Annette retires to her chambers to read it. All players still
										in the round reveal their hands. The player with the highest number in their hand wins the round. In case of a tie,
										the player who laid out the cards with the higher total value wins. A round also ends if all players but one are out of the round,
										in which case the remaining player wins.
	            		      		</b></p>
	            		      		<p><b>
	            		      		After a round ends, the winner receives a heart. All 16 cards are shuffled, and play a new round
            		    		    following all of the setup rules above. The winner of the previous round goes first,
            		    		    because the Princess speaks kindly of him or her at breakfast.
	            		      		</b></p>
	            		      		<h3>-Winning the Game</h3>
									<p><b>
	            		      			The player wins the game after getting a certain number of hearts. The number of hearts is depending on the number of players:
	            		      		</b></p>
				            			<ul><b>
											<li>2 players - 7 hearts</li>
											<li>3 players - 5 hearts</li>
											<li>4 players - 4 hearts</li>
										</b></ul>
								</div>
		            			<div class="box">
			            			<h2>CARD EFFECTS</h2>
			            			<p><b>
	            		      			Each card features a key character in the Series and text describing the card’s effect.
										The number in the lower left corner of each card shows how many of that card are in game. 
									</b></p>
								</div>
								<div class="cardsContainer">
			            			<img src=%s>
			            			<img src=%s>
			            			<img src=%s>
			            			<img src=%s>
			            			<img src=%s>
			            			<img src=%s>
			            			<img src=%s>
			            			<img src=%s>
			            		</div>
		            		</div>	
            			</body>
            		</html>
            		""".formatted(cssPath, introductionBorder, guard, priest, baron, handmaid, prince, king, countess, princess);
	            
	            webView.getEngine().loadContent(html);
	            Scene scene = new Scene(webView);
	            jfxPanel.setScene(scene);
	        });
	}
}
