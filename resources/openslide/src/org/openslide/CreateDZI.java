package org.openslide;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateDZI {

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
		int levels = (int) (Math.log(44000)/Math.log(2)) + 1;
		File root = new File("/home/ravikiran/bigBIF");
		for(int i = 0; i <= levels; i++ ){
			File dir = new File(root.getAbsolutePath() + File.separator + i );
			if(!dir.exists()){
				dir.mkdir();
			}
		}
		int bif_levels = os.getLevelCount();
		System.out.println(""+ levels+ "bif"+ bif_levels);
		for(int i = bif_levels - 1; i >=0; i--){
			/*File dir = new File("/home/ravikiran/bigBIF/" + (levels-1 - i) );
			if(!dir.exists()){
				dir.mkdir();
			}*/
			generateTiles0(os,i,256,root.getAbsolutePath() + File.separator + (levels  - i) + File.separator );
		}
		int thumbs = levels - bif_levels;
		for(int i = 0; i <= thumbs; i++ ){
			try {
				BufferedImage img = os.createImage(bif_levels - 1, 0, 0, (int)os.getLevelWidth(bif_levels-1), (int)os.getLevelHeight(bif_levels-1));
				BufferedImage simg =  getDownsampledImage(img,i,thumbs+1);
				ImageIO.write(simg, "jpg", new File(root.getAbsolutePath() +  File.separator + i+ File.separator + "0_0.jpeg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void generateTiles(OpenSlide os,int level,int tileSize,String dirPath) {
		
		BufferedImage chunk = null;
		int height = (int) os.getLevelHeight(level);
		int width = (int) os.getLevelWidth(level);
		int rows = height/tileSize;
		int columns = width/tileSize;
		int x_end = width%tileSize;
		int y_end = height%tileSize;
		System.out.println(""+rows + " " + columns);
		int count = 0;
		for(int j = 0; j < rows; j++){
			for(int i =0; i < columns; i++){
				chunk = os.createImage(level,tileSize*i, tileSize*j, tileSize, tileSize);
				try {
					ImageIO.write(chunk, "jpg", new File(dirPath + File.separator +count + ".jpeg"));
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(x_end > 0){
				chunk = os.createImage(level,width-x_end, tileSize*j, x_end, tileSize);
				//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
				try {
					ImageIO.write(chunk, "jpg", new File(dirPath + File.separator + count + ".jpeg"));
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		for(int i =0; i < columns; i++){
			chunk = os.createImage(level,tileSize*i, tileSize*rows, tileSize, y_end);
			//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
			try {
				ImageIO.write(chunk, "jpg", new File(dirPath + File.separator + count + ".jpeg"));
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(x_end > 0){
			chunk = os.createImage(level,width-x_end, tileSize*rows, x_end, y_end);
			//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
			try {
				ImageIO.write(chunk, "jpg", new File(dirPath + File.separator + count + ".jpeg"));
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	}
	private static void generateTiles0(OpenSlide os,int level,int tileSize,String dir) {

		BufferedImage chunk = null;
		int height = (int) os.getLevelHeight(level);
		int width = (int) os.getLevelWidth(level);
		int rows = height/tileSize;
		int columns = width/tileSize;
		int x_end = width%tileSize;
		int y_end = height%tileSize;
		System.out.println(""+rows + " " + columns);
		int count = 0;
		for(int j = 0; j < columns; j++){
			for(int i =0; i < rows; i++){
				chunk = os.createImage(level,tileSize*j, tileSize*i, tileSize, tileSize);
				try {
					ImageIO.write(chunk, "jpg", new File(dir  + j + "_" + i + ".jpeg"));
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(y_end > 0){
				chunk = os.createImage(level,tileSize*j, height - y_end, tileSize, y_end);
				//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
				try {
					ImageIO.write(chunk, "jpg", new File(dir + j + "_" + rows + ".jpeg"));
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		for(int i =0; i < rows; i++){
			chunk = os.createImage(level,tileSize*columns, tileSize*i, x_end, tileSize);
			//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
			try {
				ImageIO.write(chunk, "jpg", new File(dir + columns + "_" + i + ".jpeg"));
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(y_end > 0){
			chunk = os.createImage(level,tileSize*columns, height-y_end,x_end, y_end);
			//chunk = createResizedCopy(chunk, requiredWidth/columns, requiredHeight/rows, true);
			try {
				ImageIO.write(chunk, "jpg", new File(dir + columns + "_" + rows + ".jpeg"));
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	
	private static BufferedImage getDownsampledImage(BufferedImage img, int level,int levels){
		
		int scale = (int) Math.pow(2, levels - level);
		int height = (int) Math.ceil((double)img.getHeight()/scale);
		int width = (int) Math.ceil((double)img.getWidth()/scale);
		BufferedImage ret = getScaledInstance(img, width, height, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
		return ret;
		
	}
	public static BufferedImage getScaledInstance(
	        BufferedImage img, int targetWidth,
	        int targetHeight, Object hint, 
	        boolean higherQuality)
	    {
	        int type =
	            (img.getTransparency() == Transparency.OPAQUE)
	            ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
	        BufferedImage ret = (BufferedImage) img;
	        int w, h;
	        if (higherQuality)
	        {
	            // Use multi-step technique: start with original size, then
	            // scale down in multiple passes with drawImage()
	            // until the target size is reached
	            w = img.getWidth();
	            h = img.getHeight();
	        }
	        else
	        {
	            // Use one-step technique: scale directly from original
	            // size to target size with a single drawImage() call
	            w = targetWidth;
	            h = targetHeight;
	        }

	        do
	        {
	            if (higherQuality && w > targetWidth)
	            {
	                w /= 2;
	                if (w < targetWidth)
	                {
	                    w = targetWidth;
	                }
	            }

	            if (higherQuality && h > targetHeight)
	            {
	                h /= 2;
	                if (h < targetHeight)
	                {
	                    h = targetHeight;
	                }
	            }

	            BufferedImage tmp = new BufferedImage(w, h, type);
	            Graphics2D g2 = tmp.createGraphics();
	            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
	            g2.drawImage(ret, 0, 0, w, h, null);
	            g2.dispose();

	            ret = tmp;
	        } while (w != targetWidth || h != targetHeight);

	        return ret;
	    }

}
