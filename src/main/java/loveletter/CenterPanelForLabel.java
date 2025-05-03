package loveletter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CenterPanelForLabel extends JPanel{

	private static final long serialVersionUID = 1L;

	public CenterPanelForLabel(JLabel name, int cardWidth, int labelHeight) {
		createCenterPanel(name, cardWidth, labelHeight);
	}
	
	private void createCenterPanel(JLabel name, int cardWidth, int labelHeight) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setPreferredSize(new Dimension(cardWidth, labelHeight));
		this.setMinimumSize(new Dimension(cardWidth, labelHeight));
		this.setMaximumSize(new Dimension(cardWidth, labelHeight));
		this.setBackground(Color.BLACK);
		this.add(Box.createHorizontalGlue()); 
		this.add(name);
		this.add(Box.createHorizontalGlue()); 
	}
}
