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
    
	private int num_items;
	private PImage p;
	private ArrayList <VisualItem> items;
	private ArrayList <VisualItem> selectedItems;
	
	static final private int MAX_THUMBNAIL_HEIGHT = 124;
	static final private int MAX_THUMBNAIL_WIDTH = 123;
	static final private int MAX_NUM_DISPLAY = 15;
	static final private int width = 620;
	static final private int height = 370;
	
	private int start_index = 0;
	private int num_pages = 0;
	private int curr_index = 0;
	
	private float my_new_height;
	private float my_new_width;
	private static final int lowXBound = 30;
	private static final int highXBound = 650;
	private static final int lowYBound = 30;
	private static final int highYBound = 400;

	private int oldListSize = 0;
	
	/**
     * Class constructor. 
	 * @param parent the instantiating Quickshow object
	 */
	public visualthumbnailUI(Quickshow parent){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
	    items = new ArrayList<VisualItem>();
	    selectedItems = new ArrayList<VisualItem>();
	}
	
	/**
	 * TODO add method header
	 */
	public void drawBackgroundCanvas(){
		parent.rect(30, 30, width, height);
	}
	
	/**
	 * TODO add method header
	 */
	public void drawThumbNails(){
		int xStartIndex = MAX_THUMBNAIL_WIDTH/2 + 30;
		int yStartIndex = MAX_THUMBNAIL_HEIGHT/2 + 30;
		float scaleFactor;
		
		if(!items.isEmpty()) {
		//Iterate through the items to display them as thumbnail
			scaleFactor = 1.0f;
			for (int i = 0, j = start_index; i < MAX_NUM_DISPLAY && j < items.size(); i++, j++){
				//Because of the thumbnail function, we can just pull the images
				p = items.get(j).getThumbnail();
				if (p.height > MAX_THUMBNAIL_HEIGHT || p.width > MAX_THUMBNAIL_WIDTH){
					if(p.height >= p.width){
						scaleFactor = 1.0f/((float) p.height/ (float) (MAX_THUMBNAIL_HEIGHT-15));
					}
					else {
						scaleFactor = 1.0f/((float) p.width/ (float) (MAX_THUMBNAIL_WIDTH-15));
					}
				}
				
				my_new_height = (float) p.height * scaleFactor;
				my_new_width = (float) p.width * scaleFactor;
				
				parent.image(p, xStartIndex, yStartIndex, my_new_width, my_new_height);
				
				
				//Move up the next index whether its video or image
				xStartIndex += MAX_THUMBNAIL_WIDTH;
				if(xStartIndex > width) {
					if(yStartIndex < height){
						yStartIndex += MAX_THUMBNAIL_HEIGHT;
						xStartIndex = MAX_THUMBNAIL_WIDTH/2 + 30;
					}
				}
			}
		}
	}
	
	/**
	 * Receive from the file browser
	 * @param vItems an ArrayList of VisualItems
	 */
	public void receiveVisualItems(ArrayList <VisualItem> vItems){
		if(debug) {
		    parent.println("Receiving items size: " + vItems.size());
		}
		
		for(int i = oldListSize; i < vItems.size(); i++){
			items.add(vItems.get(i));
		}
		oldListSize = vItems.size();
		
		num_pages = items.size()/MAX_NUM_DISPLAY + 1;
		curr_index = 1;
	}
	
	/**
	 * Selects an item for playback.
	 * @param x the x-coordinate of the mouse
	 * @param y the y-coordinate of the mouse
	 */
	public void selectImage(int x, int y){
	    int xValue = x - lowXBound;
		int yValue = y - lowYBound;
		
		int xIndex = xValue/124;
		int yIndex = yValue/123;
		int mainIndex = start_index + ((yIndex * 5) + xIndex);
	    
		if(debug) {
	        parent.println(
                "X: " + x + " Y: " + y +
                "\nGrid coord x: " + xIndex + " y: " + yIndex +
        		"\nmain index: " + mainIndex
    		);
	    }
		
//		//Make sure we are in legal range
		if(mainIndex < items.size()){
			if(!selectedItems.contains(items.get(mainIndex)))
				selectedItems.add(items.get(mainIndex));
			
			if(debug) {
			    parent.println("Added image: " + selectedItems.get(mainIndex).checkType());
			}
		}
	}
	
	/**
	 * Retrieves the selected items.
	 * @return an ArrayList containing the selected VisualItems.
	 */
	public ArrayList<VisualItem> returnSelectedItems(){
		return selectedItems;
	}
	
	/**
	 * Goes through the list and selects only images.
	 */
	public void selectAllImages(){
		if(!items.isEmpty()) {
			for(VisualItem v: items){
    			if(v.checkType().equals("image") && !selectedItems.contains(v)){
    				selectedItems.add(v);
    			}
    		}
		}
	}
	
	/**
	 * Goes through the list and selects only videos.
	 */
	public void selectAllClips(){
		if(!items.isEmpty()) {
    		for(VisualItem v: items){
    			if(v.checkType().equals("video") && !selectedItems.contains(v)){
    				selectedItems.add(v);
    			}
    		}
		}
	}
	
	/**
	 * Clears the selected visual items from the selectedList.
	 */
	public void clearSelectedItems(){
		selectedItems.clear();
	}
	
	/**
	 * Goes down one page.
	 */
	public void showNextItems(){
		start_index += 15;
		if(start_index > items.size()){
			start_index = 0;
		}
		
		curr_index = start_index/MAX_NUM_DISPLAY + 1;
	}
	
	/**
	 * Goes up one page.
	 */
	public void showPrevItems(){
		start_index -= 15;
		if(start_index < 0){
			start_index = items.size() - (items.size()%15);
		}
		
		curr_index = start_index/MAX_NUM_DISPLAY + 1;
	}
	
	/**
	 * Returns number of pages.
	 * @return integer
	 */
	public int getNumPages(){
		return num_pages;
	}
	
	/**
	 * Returns the current page number.
	 * @return integer
	 */
	public int getCurrIndex(){
		return curr_index;
	}
}