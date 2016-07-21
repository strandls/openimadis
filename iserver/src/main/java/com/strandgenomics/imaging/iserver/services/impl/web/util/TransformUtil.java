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

package com.strandgenomics.imaging.iserver.services.impl.web.util;

import java.awt.Color;

/**
 * Utilities used while transforming from server side to web client objects and
 * vice versa.
 * 
 * @author santhosh
 * 
 */
public class TransformUtil {

    /**
     * Get hex string equivalent of {@link Color}
     * 
     * @param colour
     * @return hex string
     */
    public static String getColorString(Color colour) {
        return "#" + getHexString(colour.getRed()) + getHexString(colour.getGreen()) + getHexString(colour.getBlue());
    }

    /**
     * Get two character hex string equivalent of the given colour
     * 
     * @param colour
     * @return
     */
    private static String getHexString(int colour) {
        String hexString = Integer.toHexString(colour);
        if (hexString.length() == 1)
            return "0" + hexString;
        return hexString;
    }

    /**
     * Parse the given hex string to get {@link Color} instance
     * 
     * @param hex
     *            hex string. should be of the for "#??????"
     * @param opacity
     *            opacity to use. should be between 0 and 1
     * @return color instance
     */
    public static Color parseHexString(String hex, int opacity) {
        if (!hex.startsWith("#") || hex.length() != 7)
            throw new RuntimeException("Not a valid hexstring: " + hex);
        String redString = hex.substring(1, 3);
        String greenString = hex.substring(3, 5);
        String blueString = hex.substring(5, 7);
        int red = Integer.parseInt(redString, 16);
        int green = Integer.parseInt(greenString, 16);
        int blue = Integer.parseInt(blueString, 16);
        int alpha= opacity;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Parse the given hex string to get {@link Color} instance
     * 
     * @param hex
     *            hex string. should be of the for "#??????"
     * @return color instance
     */
    public static Color parseHexString(String hex) {
        if (!hex.startsWith("#") || hex.length() != 7)
            throw new RuntimeException("Not a valid hexstring: " + hex);
        String redString = hex.substring(1, 3);
        String greenString = hex.substring(3, 5);
        String blueString = hex.substring(5, 7);
        int red = Integer.parseInt(redString, 16);
        int green = Integer.parseInt(greenString, 16);
        int blue = Integer.parseInt(blueString, 16);
        return new Color(red, green, blue);
    }
}
