package com.strandgenomics.imaging.ithreedview.imagedisplay;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	BufferedImage currImage;
	int startx;
	int starty;
	public int preferredXSize ;
	public int  preferredYSize;

	public ImagePanel(int startx,int starty)
	{
		super();
		this.startx = startx;
		this.starty = starty;
		this.currImage = null; // Image not yet set
	}

	public ImagePanel(BufferedImage img, int startx,int starty)
	{
		super();
		this.currImage = img;
		this.startx = startx;
		this.starty = starty;
		this.preferredXSize = currImage.getWidth()+10;
		this.preferredYSize = currImage.getHeight()+10;
	}

	public void setImage(BufferedImage img)
	{
		this.currImage = img;
		this.preferredXSize = currImage.getWidth()+10;
		this.preferredYSize = currImage.getHeight()+10;
	}

	public void setStartX(int startx)
	{
		this.startx = startx;
	}

	public void paintComponent(Graphics g)
	{
		if(currImage!=null)
		{
			g.drawImage(currImage, startx, starty,currImage.getWidth(),currImage.getHeight(),this);
		}
		else
		{
			System.err.println("Image not yet set in Image Panel");
		}
	}

}
