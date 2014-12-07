/**
 * @file visualthumbnailUI.java
 * @author Moses Lee, Kay Choi
 * @description Previews the VisualItems currently loaded into the Quickshow. 
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PConstants;
import processing.core.PImage;
import quickshow.datatypes.VisualItem;

public class visualthumbnailUI {
    private Quickshow parent;

    private boolean debug = true;
    
	private PImage p;
	private ArrayList <VisualItem> items;
	private ArrayList<Integer> selectedIndex;
	
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
	private static final int bounds[] = {30, 30, 650, 400};

	/**
     * Class constructor. 
	 * @param parent the instantiating Quickshow object
	 */
	public visualthumbnailUI(Quickshow parent){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
	    items = new ArrayList<VisualItem>();
	    
	    selectedIndex = new ArrayList<Integer>();
	}
	
	/**
	 * Callback method for drawing the thumbnails of the loaded items.
	 */
	public void drawThumbNails() {
		parent.rectMode(PConstants.CORNER);
    	parent.imageMode(PConstants.CENTER);
		
    	parent.stroke(0);
		parent.fill(90,90,90);
    	parent.rect(bounds[0], bounds[1], width, height);
		
		int xStartIndex = MAX_THUMBNAIL_WIDTH/2 + 34;
		int yStartIndex = MAX_THUMBNAIL_HEIGHT/2 + 29;
		float scaleFactor;
		
		if(!items.isEmpty()) {
		//Iterate through the items to display them as thumbnail
			scaleFactor = 1.0f;
			for (int i = 0, j = start_index; i < MAX_NUM_DISPLAY && j < items.size(); i++, j++){
				//Because of the thumbnail function, we can just pull the images
				p = items.get(j).getThumbnail();
				if (p.height > MAX_THUMBNAIL_HEIGHT || p.width > MAX_THUMBNAIL_WIDTH){
					if(p.height >= p.width){
						scaleFactor = 1.0f*(MAX_THUMBNAIL_HEIGHT-15)/p.height;
					}
					else {
						scaleFactor = 1.0f*(MAX_THUMBNAIL_WIDTH-15)/p.width;
					}
				}
				
				my_new_height = (float) p.height * scaleFactor;
				my_new_width = (float) p.width * scaleFactor;

				parent.image(p, xStartIndex, yStartIndex, my_new_width, my_new_height);
				
				//selected highlight
				if(!selectedIndex.isEmpty() && selectedIndex.contains(j)) {
					parent.stroke(0xff5522ff);
					parent.noFill();
					parent.rectMode(PConstants.CENTER);
					parent.rect(xStartIndex, yStartIndex,
						MAX_THUMBNAIL_WIDTH - 2, MAX_THUMBNAIL_HEIGHT - 4);
					
					parent.fill(0);
					parent.text(
						selectedIndex.indexOf(j)+1,
						10 + xStartIndex - MAX_THUMBNAIL_WIDTH/2,
						15 + yStartIndex - MAX_THUMBNAIL_HEIGHT/2
					);
				}

				//Move up the next index whether its video or image
				xStartIndex += MAX_THUMBNAIL_WIDTH;
				if(xStartIndex > width) {
					if(yStartIndex < height){
						yStartIndex += MAX_THUMBNAIL_HEIGHT;
						xStartIndex = MAX_THUMBNAIL_WIDTH/2 + 34;
					}
				}
			}
		}
	}
	
	/**
	 * Adds the loaded visual items to the available item list.
	 * @param vItems an ArrayList of VisualItems
	 */
	public void receiveVisualItems(ArrayList <VisualItem> vItems){
		if(debug) {
			Quickshow.println("Receiving items size: " + vItems.size());
		}

		int i;
		VisualItem item;
		for(VisualItem vItem : vItems) {
			for(i = 0; i < items.size(); i++) {
				item = items.get(i);
				
				if(vItem.getFileName().equalsIgnoreCase(item.getFileName())) {
					break;
				}
			}
			
			if(i == items.size()) {
				items.add(vItem);
			}
		}
		
		num_pages = (int)Math.ceil(1f*items.size()/MAX_NUM_DISPLAY);
		curr_index = 1;
	}
	
	/**
	 * Selects an item for playback.
	 * @param x the x-coordinate of the mouse
	 * @param y the y-coordinate of the mouse
	 */
	public void selectImage(int x, int y){
	    int xValue = x - bounds[0];
		int yValue = y - bounds[1];
		
		int xIndex = xValue/124;
		int yIndex = yValue/123;
		int mainIndex = start_index + ((yIndex * 5) + xIndex);
	    
		if(debug) {
	        Quickshow.println(
                "X: " + x + " Y: " + y +
                "\nGrid coord x: " + xIndex + " y: " + yIndex +
        		"\nmain index: " + mainIndex
    		);
	    }
		
		//Make sure we are in legal range
		if(mainIndex < items.size()) {
			if(!selectedIndex.contains(mainIndex)) {
				selectedIndex.add(mainIndex);	

        		if(debug) {
        		    Quickshow.println(items.get(mainIndex).checkType() +
        	    		" added to timeline");
        		}
			}
			
			else {
				selectedIndex.remove(selectedIndex.indexOf(mainIndex));
				
				if(items.get(mainIndex).checkType().equalsIgnoreCase("image")) {
					((quickshow.datatypes.ImageItem)items.get(mainIndex))
						.setDisplayTime(5);
				}
				
				if(debug) {
					Quickshow.println(items.get(mainIndex).checkType() +
						" removed from timeline");
				}
			}
		}
	}
	
	/**
	 * Retrieves the selected items.
	 * @return an ArrayList containing the selected VisualItems.
	 */
	public ArrayList<VisualItem> returnSelectedItems() {
		ArrayList<VisualItem> result = new ArrayList<VisualItem>();
		
		for(Integer index : selectedIndex) {
			result.add(items.get(index));
		}
		return result;
	}
	
	/**
	 * Goes through the list and selects only images.
	 */
	public void selectAllImages(){
		if(!items.isEmpty()) {
			VisualItem v;
			for(int i = 0; i < items.size(); i++) {
				v = items.get(i);
    			if(v.checkType().equals("image") && !selectedIndex.contains(i)){
    				selectedIndex.add(i);
    			}
    		}
		}
	}
	
	/**
	 * Goes through the list and selects only videos.
	 */
	public void selectAllClips(){
		if(!items.isEmpty()) {
			VisualItem v;
			for(int i = 0; i < items.size(); i++) {
				v = items.get(i);
    			if(v.checkType().equals("video") && !selectedIndex.contains(i)){
    				selectedIndex.add(i);
    			}
    		}
		}
	}
	
	/**
	 * Clears the selected visual items from the selectedList.
	 */
	public void clearSelectedItems(){
		selectedIndex.clear();
	}
	
	/**
	 * Goes down one page.
	 */
	public void showNextItems(){
		start_index += 15;
		if(start_index >= items.size()){
			start_index = 0;
			curr_index = 1;
		}
		else {
			curr_index = start_index/MAX_NUM_DISPLAY + 1;
		}
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

	/**
	 * Returns the boundaries of the thumbnail window.
	 * @return an integer array with the mapping:
	 *   {left border, top border, right border, bottom border}
	 */
	public int[] getBounds() {
		return bounds;
	}
}