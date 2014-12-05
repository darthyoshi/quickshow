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
    private int prev_items_displayed = 0;
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
			
			parent.stroke((i%5 == 0 ? 0xff55aaff : 0xffffffff));
		    parent.line(x, bounds[1]+2, x, bounds[3]-2);
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
    			parent.stroke(0xffff2233);
    			parent.rectMode(PConstants.CORNER);
    			parent.rect(drawIndex, 520, time_scaled_width, new_height);
    			
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
			int tmp;
			for(int i = oldListSize; i < selectedList.size(); i++){
				itemsForDisplay.add(selectedList.get(i));

				tmp = itemsForDisplay.get(i).getDisplayTime();
				totalTime += tmp;
				Integer[] times = {totalTime - tmp, totalTime};
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
	 * TODO add method header
	 */
	public void calculateTimeLineBounds(int start){
		//Get initial draw index
		int drawIndex = bounds[0] + 5;
		curr_items_displayed = 0;
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
   		
   		if(debug) {
   		    Quickshow.println("curr_Items display: " + curr_items_displayed);
   		}
   		overall_img_length += curr_img_length;
	}
	
	/** 
	 * Removes all visual items from the timeline.
	 */
	public void clearSelectedSlides(){
		timeLineBounds.clear();
		timeStamps.clear();
		
		java.util.Iterator<VisualItem> iter = itemsForDisplay.iterator();
		VisualItem item;
		while(iter.hasNext()) {
			item = iter.next();
			item.clearTags();
			
			if(item.checkType().equalsIgnoreCase("image")) {
				((quickshow.datatypes.ImageItem)item).setDisplayTime(5);
			}
			
			iter.remove();
		}
		
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
		if(start_index > itemsForDisplay.size()) start_index = 0;
		prev_items_displayed = curr_items_displayed;
		calculateTimeLineBounds(start_index);
	}
	
	/**
	 * Goes to the previous page on the timeline.
	 * TODO make sure to wrap around back to the end of the time line
	 */
	public void showPrevOnTimeline(){
		start_index = start_index - prev_items_displayed - 1;
		if(start_index < 0) {
			start_index = 0;
			//start_index = itemsForDisplay.size() - 1;
		}
		calculateTimeLineBounds(start_index);
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
	 *   that the image is hovering over.
	 * @param x the x-coordinate of the mouse
	 * @param y the y-coordinate of the mouse
	 */
	public void displayTimeMarker(int x, int y){
		int index = getTimelineIndex(x, y);
		
		//If legal index was found then generate the marker and preview thumbnail 

		if(index > -1) {
			PImage prevThumbnail = itemsForDisplay.get(index).getThumbnail();
			parent.image(prevThumbnail, x, bounds[1]-60);
			parent.stroke(0xffff0000);
			parent.line(x, bounds[1] + 2 , x, bounds[3] - 2);
			
			if(debug) {
			    Quickshow.println("timelineBounds.size: " + timeLineBounds.size() + " start index: " + start_index);
			}
		}
	}
	
	/**
     * TODO add method header
     * @param index 
     */
    public VisualItem getItemAt(int index) {
		VisualItem result = null;
		
		//If in legal index return the visual item
		if(index > -1) {
			result = itemsForDisplay.get(index);
		}

    	return result;
	}

	/**
	 * Returns the boundaries of the timeline window.
	 * @return an integer array with the mapping:
	 *   {left border, top border, right border, bottom border}
	 */
	public int[] getBounds() {
		return bounds;
	}

	/**
	 * TODO add method header
	 * @param index
	 * @return
	 */
	public Integer[] getItemTimeStamps(int index) {
		Integer[] result = null;
		
		if(index >= 0) {
			result = timeStamps.get(index);
		}
		
		return result;
	}
	
	/**
	 * TODO add method header
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public int getTimelineIndex(int mouseX, int mouseY) {
		int index = -1;
		
		if(itemsForDisplay.size() != 0 && timeLineBounds.size() != 0){
			//Get relative to current pixel length
			float timeScale = (float) mouseX/curr_img_length;
			//Locate where in timeline to show preview
			float where = (timeScale * curr_img_length);
			
			//Find the image that falls within the bounds
			for(int i = start_index; i < timeLineBounds.size(); i++){
				if(where >= timeLineBounds.get(i)[0] && where < timeLineBounds.get(i)[1]){
					index = i;					
					break;
				}
			}
		}
		
		return index;
	}
	
	/**
	 * TODO add method header
	 * @param index
	 */
	public void updateTimeStamps(int index) {
		if(debug) {
			Quickshow.println(""+itemsForDisplay.size()+' '+timeStamps.size());
		}
		int i, tmp;
		Integer[] stamps;
		
		totalTime = 0;
		
		for(i = 0; i < index; i++) {
			totalTime += itemsForDisplay.get(i).getDisplayTime();
		}
		
		while(i < itemsForDisplay.size()) {
			tmp = itemsForDisplay.get(i).getDisplayTime();
			
			stamps = timeStamps.get(i);
			
			stamps[0] = totalTime;
			totalTime += tmp;
			stamps[1] = totalTime;
			
			i++;
		}
	}
}
