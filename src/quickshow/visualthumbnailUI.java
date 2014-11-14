package quickshow;

import processing.core.PShape;

import com.jogamp.opengl.util.packrect.Rect;

import controlP5.*;

public class visualthumbnailUI {
	int num_items;
	PShape rect1;
	
	final private int width = 500;
	final private int height = 400;
	
	public visualthumbnailUI(Quickshow q){
		
		//Draw the background canvas for the thumbnails
		q.rect(30, 30, 620, 400);
		
	}
}