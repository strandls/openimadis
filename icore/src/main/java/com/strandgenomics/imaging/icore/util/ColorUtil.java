package com.strandgenomics.imaging.icore.util;

import java.awt.Color;
import java.util.Random;

/**
 * utility class to  -interpret RGB color from a given wavelength 
 * 					 -interpret colorname(string value) from rgb color
 * @author Abishek Ahluwalia
 */
public class ColorUtil {

	
	/**
	 * interprets the color corresponding to the given wavelength
	 * @param wavelength : (unit of wavelength is nm )
	 * @return  java.awt.Color obj
	 * 2:55:57 PM
	 */
	public static Color getColor(long wavelength) {
		
		double gamma = 1.00;
		int intensityMax = 255;
		double blue;
		double green;
		double red;
		double factor;

		if(wavelength >= 350 && wavelength <= 439){
			red		= -(wavelength - 440d) / (440d - 350d);
			green 	= 0.0;
			blue	= 1.0;
		}else if(wavelength >= 440 && wavelength <= 489){
			red		= 0.0;
			green 	= (wavelength - 440d) / (490d - 440d);
			blue	= 1.0;
		}else if(wavelength >= 490 && wavelength <= 509){
			red 	= 0.0;
			green 	= 1.0;
			blue 	= -(wavelength - 510d) / (510d - 490d);
		}else if(wavelength >= 510 && wavelength <= 579){ 
			red 	= (wavelength - 510d) / (580d - 510d);
			green 	= 1.0;
			blue 	= 0.0;
		}else if(wavelength >= 580 && wavelength <= 644){
			red 	= 1.0;
			green 	= -(wavelength - 645d) / (645d - 580d);
			blue 	= 0.0;
		}else if(wavelength >= 645 && wavelength <= 780){
			red 	= 1.0;
			green 	= 0.0;
			blue	= 0.0;
		}else{
			System.out.println("Not in visible spectrum");
			
		    red 	= 0.0;
			green 	= 0.0;
			blue 	= 0.0;
		}
		
		if(wavelength >= 350 && wavelength <= 419){
			factor = 0.3 + 0.7*(wavelength - 350d) / (420d - 350d);
		}else if(wavelength >= 420 && wavelength <= 700){
			factor = 1.0;
	    }else if(wavelength >= 701 && wavelength <= 780){
			factor = 0.3 + 0.7*(780d - wavelength) / (780d - 700d);
	    }else{
		    factor = 0.0;
	    }

		int r = factorAdjust(red, factor, intensityMax, gamma);
		int g = factorAdjust(green, factor, intensityMax, gamma);
		int b = factorAdjust(blue, factor, intensityMax, gamma);

		
		Color c = 	new Color(r , g , b); 
		
		return c ;
	}

	/*****
	 * Used to adjust the R , G and B values( one at a time ) of color passed. Converts them from 0 - 1 scale to 0 - 255 scale 
	 * @param color  : double value of the color on 0-1 scale
	 * @param factor : depending ondiff wavelength regions the factor with which color needs to be multiplied
	 * @param intensityMax : maximum intensity set to 255 in getColor(long wavelength) function above
	 * @param gamma : set to 1 in getColor(long wavelength) above
	 * @return adjusted value of the color passed to this function 
	 * 1:19:27 PM
	 */
	
	private static int factorAdjust(double color, double factor, int intensityMax, double gamma){
		if(color == 0.0){
		    return 0;
		}else{
			return (int) Math.round(intensityMax * Math.pow(color * factor, gamma));
		}
	}		
	
	/**
	 * Returns out the colorname for the java.awt.Color obj passed as the parameter
	 * @param color : java.awt.Color obj whose name is to be identified
	 * @return colorName (String value) 
	 * 1:10:04 PM
	 */
	public static String getColorName(Color color){
		if(color.equals(color.BLUE)){
			System.out.println("Color is : 	BLUE" );
			return "blue";
		}else if(color.equals(color.GREEN)){
			System.out.println("Color is : 	GREEN" );
			return "green";
		}else if(color.equals(color.RED)){
			System.out.println("Color is : 	RED" );
			return "red";
		}else if(color.equals(color.CYAN)){
			System.out.println("Color is : 	CYAN" );
			return "cyan";
		}else if(color.equals(color.YELLOW)){
			System.out.println("Color is : 	YELLOW" );
			return "yellow";
		}else if(color.equals(color.MAGENTA)){
			System.out.println("Color is : MAGENTA" );
			return "magenta";
		}else {
			System.out.println("ILLEGAL Color" );
		
		
		return null ;
		}
		
	}
	
	
	/**
	 * @param args2:46:34 PM
	
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Color.BLUE);
		System.out.println(Color.RED);
		System.out.println(Color.GREEN);
		System.out.println(Color.YELLOW);
		System.out.println(Color.MAGENTA);
		System.out.println(Color.CYAN);
		long wavelength[] = {461,509 , 580,670};
		for (int i = 0 ;  i < wavelength.length ; i++){
			Random a = new Random();
			//long wavelength = (long) (350 + (long)380*a.nextDouble());
			Color color = getColor(wavelength[i]);
			System.out.println("Color for wavelength " + wavelength[i] +  " : " + color
					);
			String name = getColorName(color);
		}

	}

}
