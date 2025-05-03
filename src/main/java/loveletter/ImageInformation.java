package loveletter;
import javax.swing.JLabel;

public class ImageInformation {
	private final JLabel label;
	private final int width;
	private final int height;

    public ImageInformation(JLabel label, int width, int height) {
        this.label = label;
        this.width = width;
        this.height = height;
    }
    
    public JLabel getLabel() { return this.label; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
}
