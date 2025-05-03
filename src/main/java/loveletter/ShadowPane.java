package loveletter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ShadowPane extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private int activeCount;
	
	public ShadowPane(JLayeredPane layeredPane) {
		activeCount = 0;
		int width = layeredPane.getWidth();
		int height = layeredPane.getHeight();
		this.setBounds(0,0,width,height);
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		
		this.addMouseListener(new MouseAdapter() {
			@Override
	        public void mousePressed(MouseEvent e) {
	            e.consume();
	        }
		});
		
		setOpaque(false);
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());

	}
	
	public void addCount() {
        activeCount++;
        updateVisibility();
    }

    public void decreaseCount() {
        activeCount = Math.max(0, activeCount - 1);
        updateVisibility();
    }
    
    public void resetCount() {
        activeCount = 0;
        updateVisibility();
    }

    private void updateVisibility() {
        setVisible(activeCount > 0);
    }
	
}
