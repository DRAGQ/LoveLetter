package loveletter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class EscMenuButton extends JButton {

	private static final long serialVersionUID = 1L;
	private int radius = 10;
	private Color color;
	private Color borderColor;
	
	public EscMenuButton(String buttonName) {
		this.setText(buttonName);
		this.setForeground(Color.WHITE);
		this.color = new Color(0, 0, 153, 170);
		this.borderColor = new Color(0, 0, 100, 200);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        g2.setColor(color);  
        g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, radius, radius);
        super.paintComponent(g);

	}

}
