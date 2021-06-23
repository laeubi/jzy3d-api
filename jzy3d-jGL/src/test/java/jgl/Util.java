package jgl;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Util {
	/* ******************** DEBUG ********************/

	public static void debugWriteImageTo(GL gl, String file) {

		if(gl instanceof jgl.awt.GL) {
			debugWriteImageTo(file, ((jgl.awt.GL)gl).getRenderedImage());
		}
	}

	private static void debugWriteImageTo(String file, RenderedImage image) {

		try {
			// JavaImage.getGraphics().drawString("COUCOU", 200, 200);
			// String debugImg = "target/GL.png";
			ImageIO.write(image, "png", new File(file));
			System.err.println("GL write image buffer to : " + file);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
