package org.openslide;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage b = ImageIO.read(new File("/home/ravikiran/testimage.png"));
		gridImage(b,256);
	}
	private static void gridImage(BufferedImage img,int size){
		BufferedImage chunk = null;
		int height = img.getHeight();
		int width = img.getWidth();
		int rows = height/size;
		int columns = width/size;
		int x_end = width%size;
		int y_end = height%size;
		System.out.println(""+rows + " " + columns);
		int count = 0;
		for(int j = 0; j < rows; j++){
			for(int i =0; i < columns; i++){
				chunk = img.getSubimage(size*i, size*j, size, size);
				//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
				try {
					ImageIO.write(chunk, "png", new File("/home/ravikiran/Pictures/Testing/" + count + ".png"));
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(x_end > 0){
				chunk = img.getSubimage(width-x_end, size*j, x_end, size);
				//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
				try {
					ImageIO.write(chunk, "png", new File("/home/ravikiran/Pictures/Testing/" + count + ".png"));
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		for(int i =0; i < columns; i++){
			chunk = img.getSubimage(size*i, size*rows, size, y_end);
			//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
			try {
				ImageIO.write(chunk, "png", new File("/home/ravikiran/Pictures/Testing/" + count + ".png"));
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(x_end > 0){
			chunk = img.getSubimage(width-x_end, size*rows, x_end, y_end);
			//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
			try {
				ImageIO.write(chunk, "png", new File("/home/ravikiran/Pictures/Testing/" + count + ".png"));
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
