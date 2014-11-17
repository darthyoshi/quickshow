/**
 * @file visualthumbnailUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PImage;
import processing.core.PShape;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.MediaItem;
import quickshow.datatypes.VisualItem;

import com.jogamp.opengl.util.packrect.Rect;

import controlP5.*;

public class visualthumbnailUI {
	int num_items;
	PShape rect1;
	PImage p;
	ArrayList <VisualItem> items;
	ArrayList <VisualItem> selectedItems;
	
	final private int width = 620;
	final private int height = 370;
	
	public visualthumbnailUI(){
		items = new ArrayList<VisualItem>();
		
	}
	
	/*
	 * 
	 */
	public void drawBackgroundCanvas(Quickshow q){
		q.rect(30, 30, width, height);
	}
	
	/*
	 * 
	 */
	public void drawThumbNails(Quickshow q){
		if(items != null) {
		//Iterate through the items to display them as thumbnail
			for (VisualItem v: items){
				if(v.checkType().equals("image")) {
					p = ((ImageItem) v).getImage();
					q.image(p, 50, 50);
				}
			
			}
		}
	}
	
	/*
	 * Receive from the file browser
	 */
	public void receiveVisualItems(ArrayList <VisualItem> vItems){
		items.addAll(vItems);
	}
}