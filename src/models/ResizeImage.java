package models;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ResizeImage {
	
	public Image resImage(Image img){

		BufferedImage ri = new BufferedImage(573,285, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = ri.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, 583, 285, null);
		g.dispose();
		return ri;

	}

}
