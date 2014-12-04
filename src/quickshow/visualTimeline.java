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
    private int curr_img_length = 0;
    private int overall_img_length = 0;
    private ArrayList<Integer[]> timeStamps;
    private ArrayList<Integer[]> timeLineBounds;
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
		timeLineBounds = new ArrayList<Integer[]>();
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
    			
    			//Adjust each image to fit on timeline maintaining Aspect Ratio
    			if (image.height > timeLineHeight){
					scaleFactor = 1.0f/((float) image.height/ (float) (timeLineHeight-15));
    			}
    			
    			float new_height = scaleFactor * image.height;
    			float new_width = scaleFactor * image.width;
    			
    			int duration = itemsForDisplay.get(j).getDisplayTime();
    			float timeScaleFactor = duration/5;
    			float time_scaled_width = timeScaleFactor * new_width;
    			
    			//Draw the image and a box around it
    			parent.image(image, drawIndex, 520, time_scaled_width , new_height);
    			parent.noFill();
    			parent.stroke(0x00000000);
    			parent.rectMode(parent.CORNER);
    			parent.rect(drawIndex, 520, new_width, new_height);
    			
    			//Increment the x index
    			drawIndex += new_width;
    			curr_img_length = drawIndex;
    			
    			//Get the current duration of the current images that are drawn
    			if(curr_Time < totalTime) curr_Time += duration;
    			
    			//Check to see if the image can be drawn within the timeline
    			if(drawIndex + new_width > timeLineWidth) break;

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
			curr_Time = 0;
			calculateTimeLineBounds(oldListSize);
			oldListSize = selectedList.size();
			
			num_pages = itemsForDisplay.size()/MAX_THUMBNAIL_DISPLAY + 1;
			curr_index = 1;
		}
	}
	
	/**
	 * 
	 */
	public void calculateTimeLineBounds(int start){
		//Get initial draw index
		int drawIndex = bounds[0] + 5;
		
		//Clear the timeline
		//timeLineBounds.clear();
		
		//Go through the list and calculate placements along the time line
   		for (int j = start; j < itemsForDisplay.size(); j++){
			
			image = itemsForDisplay.get(j).getThumbnail();
			
			//Adjust each image to fit on timeline maintaining Aspect Ratio
			if (image.height > timeLineHeight){
				scaleFactor = 1.0f/((float) image.height/ (float) (timeLineHeight-15));
			}
			
			float new_width = scaleFactor * image.width;
			int duration = itemsForDisplay.get(j).getDisplayTime();
			float timeScaleFactor = duration/5.0f;
			float time_scaled_width = timeScaleFactor * new_width;
			
			drawIndex += time_scaled_width;
			curr_img_length = drawIndex;
			
			Integer [] tbBounds = {(int) (drawIndex - time_scaled_width), drawIndex};
			timeLineBounds.add(tbBounds);
			//Check to see if the image can be drawn within the timeline
			if(drawIndex + new_width > timeLineWidth) break;
			
			//Keep track of how many items are on the visualtimeline
			curr_items_displayed++;
			
   		}
   		System.out.println("curr_Items display: " + curr_items_displayed);
   		overall_img_length += curr_img_length;
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
		start_index = start_index + curr_items_displayed + 1;
		calculateTimeLineBounds(start_index);
	}
	
	/**
	 * Goes to the previous page on the timeline.
	 */
	public void showPrevOnTimeline(){

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
	 * Displays the marker in the visual timeline and the thumbnail 
	 * that the image is hovering over
	 */
	public void displayTimeMarker(int x, int y){
		if(itemsForDisplay.size() != 0){
			//Find the scale factor
			float timeScale = (float) x/curr_img_length;
			
			//Locate where in timeline to show preview
			float where = (timeScale * curr_img_length);
			int index = -1;
			PImage prevThumbnail;
			
			//Find the image that falls within the bounds
			for(int i = start_index; i < timeLineBounds.size(); i++){
				if(where >= timeLineBounds.get(i)[0] && where < timeLineBounds.get(i)[1]){
					index = i;
					break;
				}
			}
			
			//If legal index was found then generate the marker and preview thumbnail 
			if(index > -1){
				prevThumbnail = itemsForDisplay.get(index).getThumbnail();
				parent.image(prevThumbnail, x, bounds[1]-60);
				parent.stroke(0xffff0000);
				parent.line(x, bounds[1] + 2 , x, bounds[3] - 2);
				
			}
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
