/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
