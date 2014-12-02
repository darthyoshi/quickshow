/**
 * @file visualTimeline.java
 * @author Moses Lee
 * @description Renders the Quickshow timeline.
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PImage;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.VisualItem;

public class visualTimeline {
	private final static int timeLineWidth = 840;
	private final static int timeLineHeight = 65;
	private final static int thumbnailWidth = 104;
	private final static int MAX_THUMBNAIL_DISPLAY = 8;
	private int start_index = 0;
	private float scaleFactor;
	private Quickshow parent;
    private int oldListSize;
    private boolean debug;
    private int num_pages = 0;
    private int curr_index = 0;
    private int totalTime = 0;
    private PImage image;
    private final static int[] bounds = {30, 512, 870, 577};
    
    private ArrayList <VisualItem> itemsForDisplay;
   
	/**
	 * Class constructor.
	 * @param parent the instantiating Quickshow object
	 */
	public visualTimeline(Quickshow parent){
		this.parent = parent;
		itemsForDisplay = new ArrayList<VisualItem>();
		
		debug = parent.getDebugFlag();
	}
	
	/**
	 * Drawing a simple background canvas
	 */
	public void drawBackgroundCanvas() {
		parent.rect(30, 512, timeLineWidth, timeLineHeight);
	}
	
	/**
	 * Function to generate the thumb nails for the selected items vector
	 */
	public void generateThumbnails(){
		//If empty exit function
		if(!itemsForDisplay.isEmpty()) {
    		int drawIndex = thumbnailWidth/2 + 25;
    		
    		for (int i = 0, j = start_index; i < MAX_THUMBNAIL_DISPLAY && j < itemsForDisplay.size(); i++, j++){
    			image = itemsForDisplay.get(j).getThumbnail();
    			
    			//Gets the total number of time from the visual element display time
    			
    			//Adjust each image to fit on timeline maintaining Aspect Ratio
    			if (image.height > timeLineHeight || image.width > thumbnailWidth){
    				if(image.height >= image.width){
    					scaleFactor = 1.0f/((float) image.height/ (float) (timeLineHeight-15));
    				}
    				else {
    					scaleFactor = 1.0f/((float) image.width/ (float) (thumbnailWidth-35));
    				}
    			}
    			float new_height = scaleFactor * image.height;
    			float new_width = scaleFactor * image.width;
    			parent.image(image, drawIndex, 547, new_width , new_height);
    			
    			//Increment the x index
    			if(drawIndex < timeLineWidth) drawIndex+= thumbnailWidth;
    		}
		}
	}
	
	/**
	 * Receiving the visual items
	 */
	public void receiveSelectedItems(ArrayList<VisualItem> selectedList){
		if(selectedList.size() == 0) {
			oldListSize = 0;
			return;
		}
		
		for(int i = oldListSize; i < selectedList.size(); i++){
			itemsForDisplay.add(selectedList.get(i));
			totalTime += itemsForDisplay.get(i).getDisplayTime();
		}
		
		oldListSize = selectedList.size();
		
		num_pages = itemsForDisplay.size()/MAX_THUMBNAIL_DISPLAY + 1;
		curr_index = 1;
	}
	
	/**
	 * 
	 * Clear the selected slides
	 */
	public void clearSelectedSlides(){
		itemsForDisplay.clear();
		
		//Reset the display index
		start_index = 0;
		curr_index = 0;
		num_pages = 0;
		totalTime = 0;
	}
	
	/**
	 * to show next items
	 */
	public void showNextOnTimeline(){
		if(!itemsForDisplay.isEmpty()) {
			start_index += 8;
			if(start_index > itemsForDisplay.size()) start_index = 0;
			
			curr_index = start_index/MAX_THUMBNAIL_DISPLAY + 1;
		}
		
		else {
			start_index = curr_index = 0;
		}
	}
	
	/**
	 * TODO add method header
	 */
	public void showPrevOnTimeline(){
		if(!itemsForDisplay.isEmpty()) {
			start_index -= 8;
			if(start_index < 0) 
				start_index = itemsForDisplay.size() - (itemsForDisplay.size()%MAX_THUMBNAIL_DISPLAY);
			curr_index = start_index/MAX_THUMBNAIL_DISPLAY + 1;
		}
		
		else {
			start_index = curr_index = 0;
		}
	}
	
	/**
	 * Returns the total length of time for the slide show
	 */
	public int getTotalTime(){
		return totalTime;
	}
	
	/**
	 * Getters for num_pages and curr_index
	 */
	public int getNumPages(){
		return num_pages;
	}
	public int getCurrIndexPages(){
		return curr_index;
	}
	
	/**
     * Handler for mouse click. Selects a single timeline item if applicable.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void mouseClicked(int mouseX, int mouseY) {
		//TODO implement
	}

	/**
	 * Returns the boundaries of the timeline window.
	 * @return an integer array with the mapping:
	 *   {left border, top border, right border, bottom border}
	 */
	public int[] getBounds() {
		return bounds;
	}
}
