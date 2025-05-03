package loveletter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class PlayerPlayedCardsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private float transparency = 0.0f;
	
	public PlayerPlayedCardsPanel(Dimension size) {
		this.setLayout(null);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
        setOpaque(false);
    }
	
	public void setTransparency(float alpha) {
        this.transparency = alpha;
        repaint();
    }
	
	 @Override
	    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (transparency > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }
}
