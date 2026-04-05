/**
 * 
 */
package br.com.meslin.model;

import java.awt.Color;

/**
 * @author meslin
 *
 */

public class ColorSet {
	private static final int TRANSPARENCY = 32;
	private static Color[] color = {
			new Color(  0,   0,   0, TRANSPARENCY),
			new Color(  0,   0, 255, TRANSPARENCY),
			new Color(  0, 255,   0, TRANSPARENCY),
			new Color(  0, 255, 255, TRANSPARENCY),
			new Color(255,   0,   0, TRANSPARENCY),
			new Color(255,   0, 255, TRANSPARENCY),
			new Color(255, 255,   0, TRANSPARENCY),
			new Color(255, 255, 255, TRANSPARENCY),
			new Color(  0,   0, 128, TRANSPARENCY),
			new Color(  0, 128,   0, TRANSPARENCY),
			new Color(  0, 128, 128, TRANSPARENCY),
			new Color(128,   0,   0, TRANSPARENCY),
			new Color(128,   0, 128, TRANSPARENCY),
			new Color(128, 128,   0, TRANSPARENCY),
			new Color(128, 128, 128, TRANSPARENCY)
	};
	
	public static Color getColor(int number) {
		return color[number % color.length];
	}
}
