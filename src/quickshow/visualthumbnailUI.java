/**
 * @file visualthumbnailUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import processing.core.PShape;

import com.jogamp.opengl.util.packrect.Rect;

import controlP5.*;

public class visualthumbnailUI {
	int num_items;
	PShape rect1;
	
	final private int width = 620;
	final private int height = 370;
	
	public visualthumbnailUI(){

		
	}
	
	public void drawBackgroundCanvas(Quickshow q){
		q.rect(30, 30, width, height);
	}
}