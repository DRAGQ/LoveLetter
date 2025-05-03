package loveletter;
import java.util.List;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class CardListenerContext {
    private JLayeredPane layeredPane;
    private JPanel activePlayerCardsPanel;
    private List<PlayerGui> playersPanel;

    public CardListenerContext(JLayeredPane layeredPane, JPanel activePlayerCardsPanel, List<PlayerGui> playersPanel) {
        this.layeredPane = layeredPane;
        this.activePlayerCardsPanel = activePlayerCardsPanel;
        this.playersPanel = playersPanel;
    }

    public JLayeredPane getLayeredPane() { return layeredPane; }
    public JPanel getActivePlayerCardsPanel() { return activePlayerCardsPanel; }
    public List<PlayerGui> getPlayersPanel() { return playersPanel; }
}
