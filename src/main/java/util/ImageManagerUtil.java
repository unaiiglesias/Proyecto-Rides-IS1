package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageManagerUtil {
	
	public static ImageIcon readImageFromFile (String path) {
		System.out.println("Rading: " + path);
		return new ImageIcon(path);
	}
	
	public static ImageIcon convertByteArrToIcon (byte[] arr) {
		return new ImageIcon(arr);
	}
	
	public static byte[] convertImageIconToByteArr (ImageIcon icon) {
		/**
		 * Converting from ImageIcon to byte[] is trivial, but going the other way
		 * is not so easy. This method handles it
		 * 
		 * Used to write user generated ImageIcon to database writable byte []
		 * 
		 * Will return null if read failed
		 */
		
		String format = "png";
		// Extract the Image itself
		Image img = icon.getImage();
		
		BufferedImage buffImg;
		try {
			// From here on, we'll do a bunch of transformations we don't fully understand but that
			// have been expertly recommended by our beloved ChatGPT <3
			buffImg = new BufferedImage(
		            icon.getIconWidth(),
		            icon.getIconHeight(),
		            BufferedImage.TYPE_INT_ARGB // Add transparency support
	        );			
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
		
		
		Graphics2D g2d = buffImg.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
        	ImageIO.write(buffImg, format, baos);
        }
        catch (IOException e)
        {
        	// I hope this doesn't happen
        	System.out.println("Image conversion failed for " + icon.getDescription());
        }
        
        return baos.toByteArray();
        
	}
	
	public static ImageIcon getDefaultIcon () {
		return readImageFromFile("src/main/resources/profile.png");
	}
	
	public static byte[] getDefaultIconArr() {
		return convertImageIconToByteArr(getDefaultIcon());
	}
	
	public static String verifyIcon (ImageIcon icon) {
		/**
		 * Verifies that icon is correct
		 * 
		 * Returns null if everything OK, string detailing error otherwise
		 */
		
		if (icon.getIconWidth() != 64 || icon.getIconHeight() != 64)
			return ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.iconSizeError");
		
		return null;
	}
	
	public static void main (String[] args) {
		// Debugging purposes only
		// TODO: Debug something
		System.out.println("TEST");
	}
}
