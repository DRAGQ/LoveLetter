package loveletter;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {

	public static void main(String[] args) {
		addLookAndFeel();
		Game game = new Game(new Deck());
		GameController controller = new GameController(game);
		new GameSetUpGui(controller);
	}

	private static void addLookAndFeel() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    System.out.println("LookAndFeel is not loaded.");
		}
	}
}
