/**
 * @file visualthumbnailUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PShape;
import quickshow.datatypes.VisualItem;

import com.jogamp.opengl.util.packrect.Rect;

import controlP5.*;

public class visualthumbnailUI {
	int num_items;
	PShape rect1;
	ArrayList <VisualItem> items;
	
	final private int width = 620;
	final private int height = 370;
	
	public visualthumbnailUI(){

		
	}
	
	public void drawBackgroundCanvas(Quickshow q){
		q.rectMode(q.CORNER);
		q.rect(30, 30, width, height);
	}
}