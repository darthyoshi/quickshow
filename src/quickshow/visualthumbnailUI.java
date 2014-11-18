/**
 * @file visualthumbnailUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PImage;
import processing.core.PShape;
import quickshow.datatypes.*;


import controlP5.*;

public class visualthumbnailUI {
    private Quickshow parent;
    
	int num_items;
	PShape rect1;
	PImage p;
	ArrayList <VisualItem> items;
	ArrayList <VisualItem> selectedItems;
	
	private final int MAX_THUMBNAIL_HEIGHT = 124;
	private final int MAX_THUMBNAIL_WIDTH = 123;
	final private int width = 620;
	final private int height = 370;
	
	private float my_new_height;
	private float my_new_width;
	private int xPlacementCounter;
	private int yPlacementCounter;
	private float scaleFactor = 1.0f;
	

	public visualthumbnailUI(Quickshow parent){
	    this.parent = parent;
	    items = new ArrayList<VisualItem>();
	    selectedItems = new ArrayList<VisualItem>();
	}
	
	/*
	 * 
	 */
	public void drawBackgroundCanvas(){
		parent.rect(30, 30, width, height);
	}
	
	/*
	 * 
	 */
	public void drawThumbNails(){
		if(items != null) {
		//Iterate through the items to display them as thumbnail
			scaleFactor = 1.0f;
			for (VisualItem v: items){
				if(v.checkType().equals("image")) {
					p = ((ImageItem) v).getImage();
					if (p.height > MAX_THUMBNAIL_HEIGHT || p.width > MAX_THUMBNAIL_WIDTH){
						if(p.height > p.width){
							scaleFactor = (float) MAX_THUMBNAIL_HEIGHT/ (float) p.height;
						}
						else {
							scaleFactor = (float) MAX_THUMBNAIL_WIDTH/ (float) p.width;
						}
						
					}
					my_new_height = (float) p.height * scaleFactor;
					my_new_width = (float) p.width * scaleFactor;
					parent.image(p, 80, 80, my_new_height, my_new_width);
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
	
	public void selectImage(int x, int y){
		System.out.println("X: " + x + " Y: " + y);
		
		//Only do it for one image
		if(x < (34 + 124) && y < (37 + 124)){
			selectedItems.add(items.get(0));
			System.out.println("Added image: " + selectedItems.get(0).checkType());
		}
	}
	
	public ArrayList<VisualItem> returnSelectedItems(){
		return selectedItems;
	}
}