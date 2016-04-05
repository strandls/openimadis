package org.openslide;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OpenSlide os = null;
		File f = new File("/home/ravikiran/SI-09.bif");
		try {
			os = new OpenSlide(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int level = 1;
		long w = os.getLevelWidth(level);
		long h = os.getLevelHeight(level);
		
		//BufferedImage img = new BufferedImage(1401,765,BufferedImage.TYPE_INT_RGB);
		BufferedImage img = new BufferedImage((int)w, (int)h,
                BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
        int data[] = ((DataBufferInt) img.getRaster().getDataBuffer())
                .getData();

        try {
			os.paintRegionARGB(data, 0, 0, level, img.getWidth(), img
			        .getHeight());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        os.close();
		File outputfile = new File("/home/ravikiran/testimage.png");
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("width=" + img.getWidth() + "height" + img.getHeight());
	}

}
