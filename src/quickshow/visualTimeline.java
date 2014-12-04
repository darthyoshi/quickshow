/**
 * @file visualTimeline.java
 * @author Moses Lee, Kay Choi
 * @description Renders the Quickshow visual item timeline.
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PConstants;
import processing.core.PImage;
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
    private int curr_items_displayed = 0;
    private int totalTime = 0;
    private int curr_Time = 0;
    private ArrayList<Integer[]> timeStamps;
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
		timeStamps = new ArrayList<Integer[]>();
		debug = parent.getDebugFlag();
	}
	
	/**
	 * Drawing a simple background canvas
	 */
	public void drawBackgroundCanvas() {
		parent.rectMode(PConstants.CORNER);
    	parent.imageMode(PConstants.CENTER);
    	
    	parent.fill(90,90,90);
		parent.stroke(0);
		parent.rect(30, 512, timeLineWidth, timeLineHeight);
		
		parent.stroke(0xffffffff);
		short x;
		for(short i = 1; i < 30; i++) {
			x = (short)(i*28 + 30);
			
            if(i%5 == 0) {
                parent.line(x, (bounds[1]+bounds[3])/2-22, x, (bounds[1]+bounds[3])/2+22);
            }
            else {
                parent.line(x, (bounds[1]+bounds[3])/2-7, x, (bounds[1]+bounds[3])/2+7);
            }
		}
	}
	
	/**
	 * Generates thumbnails for the selected items.
	 */
	public void generateThumbnails(){
		//If empty exit function
		if(!itemsForDisplay.isEmpty()) {
			parent.imageMode(PConstants.CORNER);
    	
    		int drawIndex = bounds[0] + 5;
    		
    		for (int j = start_index; j < itemsForDisplay.size(); j++){
  
    				
    			image = itemsForDisplay.get(j).getThumbnail();
    			
    			//Gets the total number of time from the visual element display time
    			
    			//Adjust each image to fit on timeline maintaining Aspect Ratio
    			if (image.height > timeLineHeight){
					scaleFactor = 1.0f/((float) image.height/ (float) (timeLineHeight-15));
    			}
    			
    			float new_height = scaleFactor * image.height;
    			float new_width = scaleFactor * image.width;
    			
    			int duration = itemsForDisplay.get(j).getDisplayTime();
    			float timeScaleFactor = duration/5;
    			new_width = timeScaleFactor * new_width;
    			
    			curr_Time += duration;
    			
    			parent.image(image, drawIndex, 520, new_width , new_height);
    			parent.noFill();
    			parent.stroke(0x00000000);
    			parent.rectMode(parent.CORNER);
    			parent.rect(drawIndex, 520, new_width, new_height);
    			
    			//Increment the x index
    			drawIndex += new_width;
    			
    			//Check to see if the image can be drawn within the timeline
    			if(drawIndex + new_width > timeLineWidth) break;
    			
    			//Keep track of how many items are on the visualtimeline
    			curr_items_displayed++;
    		}
		}
	}
	
	/**
	 * Adds the selected visual items to the timeline.
	 * @param selectedList an ArrayList containing the selected VisualItems
	 */
	public void receiveSelectedItems(ArrayList<VisualItem> selectedList){
		if(selectedList.isEmpty()) {
			oldListSize = 0;
		}
		
		else {
			for(int i = oldListSize; i < selectedList.size(); i++){
				itemsForDisplay.add(selectedList.get(i));

				totalTime += itemsForDisplay.get(i).getDisplayTime();
				Integer[] times = {totalTime - totalTime, totalTime};
				timeStamps.add(times);
			}
			
			oldListSize = selectedList.size();
			
			num_pages = itemsForDisplay.size()/MAX_THUMBNAIL_DISPLAY + 1;
			curr_index = 1;
		}
	}
	
	/** 
	 * Removes all visual items from the timeline.
	 */
	public void clearSelectedSlides(){
		itemsForDisplay.clear();
		
		//Reset the display index
		start_index = 0;
		curr_index = 0;
		num_pages = 0;
		totalTime = 0;
		curr_items_displayed = 0;
	}
	
	/**
	 * Goes to the next page on the timeline.
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
	 * Goes to the previous page on the timeline.
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
	 * 
	 * 
	 */
	public void displayTimeMarker(int x, int y){
		if(itemsForDisplay.size() != 0){
			System.out.println("What is x: " + x);
			float timeScale = x/timeLineWidth;
			float where = (timeScale * curr_Time);
			int index = -1;
			PImage prevThumbnail;
			
			for(int i = 0; i < timeStamps.size(); i++){
				if(where >= timeStamps.get(i)[0] && where < timeStamps.get(i)[1]){
					index = i;
					break;
				}
			}
			
			System.out.println("Where: " + where + "   Index: " + index + "  timescale: " + timeScale);
			
			if(index > -1){
				prevThumbnail = itemsForDisplay.get(index).getThumbnail();
				parent.image(prevThumbnail, x, bounds[1]-60);
			}
			parent.stroke(0xffff0000);
			parent.line(x, bounds[1] + 2 , x, bounds[3] - 2);
			
		}
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
