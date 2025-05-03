package loveletter;
import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.awt.GraphicsEnvironment;

import javax.swing.JLabel;

public class CustomTextFont {
	public static JLabel createFont(String text) {
        JLabel textLabel = new JLabel(text);

        try (InputStream is = CustomTextFont.class.getResourceAsStream("/textFont/playerName/Kablammo-Regular-VariableFont_MORF.ttf")) {
			if (is == null) {
				System.err.println("Font file not found in resources!");
				return textLabel;
			}

			Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
			textLabel.setFont(customFont);
			textLabel.setForeground(Color.decode("#e1c6a9"));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return textLabel;
    }
}