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

    private boolean debug = true;
    
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
	//private float scaleFactor = 1.0f;
	final int lowXBound = 30;
	final int highXBound = 650;
	final int lowYBound = 30;
	final int highYBound = 400;
	

	public visualthumbnailUI(Quickshow parent){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
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
		int xStartIndex = 95;
		int yStartIndex = 95;
		float xScaleFactor;
		float yScaleFactor;
		if(items != null) {
		//Iterate through the items to display them as thumbnail
			xScaleFactor = 1.0f;
			yScaleFactor = 1.0f;
			for (VisualItem v: items){
				if(v.checkType().equals("image")) {
					p = ((ImageItem) v).getImage();
					if (p.height > MAX_THUMBNAIL_HEIGHT || p.width > MAX_THUMBNAIL_WIDTH){
						//if(p.height > p.width){
							xScaleFactor = 1.0f/((float) p.height/ (float) MAX_THUMBNAIL_HEIGHT);
						//}
						//else {
							yScaleFactor = 1.0f/((float) p.width/ (float) MAX_THUMBNAIL_WIDTH);
					//	}
					}
					
					System.out.println("x scaleFactor is: " + xScaleFactor);
					my_new_height = (float) p.height * xScaleFactor;
					my_new_width = (float) p.width * yScaleFactor;
					
					parent.image(p, xStartIndex, yStartIndex, my_new_height, my_new_width);
					
					//Move up the next index
					xStartIndex += MAX_THUMBNAIL_WIDTH;
					if(xStartIndex > width) {
						xStartIndex = 90;
						yStartIndex += MAX_THUMBNAIL_HEIGHT;
					}
				}
			}
		}
	}
	
	/*
	 * Receive from the file browser
	 */
	public void receiveVisualItems(ArrayList <VisualItem> vItems){
		items.clear();
		items.addAll(vItems);
	}
	
	/*
	 * 
	 */
	public void selectImage(int x, int y){
		System.out.println("X: " + x + " Y: " + y);
		
		int xValue = x - lowXBound;
		int yValue = y - lowYBound;
		
		int xIndex = xValue/124;
		int yIndex = yValue/123;
		int mainIndex = yIndex * 5 + xIndex;
		
		System.out.println("Grid coord x: " + xIndex + " y: " + yIndex);
		System.out.println("main index: " + mainIndex);
		
//		//Make sure we are in legal range
		if(mainIndex < items.size()){
			selectedItems.add(items.get(mainIndex));
			
			if(debug) {
			    //parent.println("Added image: " + selectedItems.get(mainIndex).checkType());
			}
		}
	}
	
	public ArrayList<VisualItem> returnSelectedItems(){
		return selectedItems;
	}
	
	/**
	 * TODO for demo - retrieves all visualItems
	 * @return
	 */
	public ArrayList<VisualItem> getVisualItems() {
	    return items;
	}
}