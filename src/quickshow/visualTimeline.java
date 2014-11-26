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
    private PImage image;
    
    private ArrayList <VisualItem> itemsForDisplay;
   
	/**
	 * Empty Constructor need to some how do something
	 */
	public visualTimeline(Quickshow parent){
		this.parent = parent;
		itemsForDisplay = new ArrayList<VisualItem>();
		
		debug = parent.getDebugFlag();
	}
	
	/**
	 * Drawing a simple background canvas
	 */
	public void drawBackgroundCanvas(){
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
    			parent.image(image, drawIndex, 533, new_width , new_height);
    			
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
	}
	
	/**
	 * to show next items
	 */
	public void showNextOnTimeline(){
		start_index += 8;
		if(start_index > itemsForDisplay.size()) start_index = 0;
		
		curr_index = start_index/MAX_THUMBNAIL_DISPLAY + 1;
	}
	
	/**
	 * TODO add method header
	 */
	public void showPrevOnTimeline(){
		start_index -= 8;
		if(start_index < 0) 
			start_index = itemsForDisplay.size() - (itemsForDisplay.size()%MAX_THUMBNAIL_DISPLAY);
		curr_index = start_index/MAX_THUMBNAIL_DISPLAY + 1;
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
}
