package quickshow;

import java.util.ArrayList;

import processing.core.PImage;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.VisualItem;

public class visualTimeline {
	private final int timeLineWidth = 840;
	private final int timeLineHeight = 65;
	private final int thumbnailWidth = 104;
	private final int MAX_THUMBNAIL_DISPLAY = 8;
	private int start_index = 0;
	private float scaleFactor;
	private Quickshow parent;
    private int oldListSize;
    private boolean debug;
    PImage image;
    
	ArrayList <VisualItem> itemsForDisplay;
   
   
	
	/*
	 * Empty Constructor need to some how do something
	 */
	public visualTimeline(Quickshow parent){
		this.parent = parent;
		itemsForDisplay = new ArrayList<VisualItem>();
		
		debug = parent.getDebugFlag();
	}
	
	/*
	 * Drawing a simple background canvas
	 */
	public void drawBackgroundCanvas(){
		parent.rect(30, 500, timeLineWidth, timeLineHeight);
	}
	
	/*
	 * Function to generate the thumb nails for the selected items vector
	 */
	public void generateThumbnails(){
		//If empty exit function
		if(itemsForDisplay.size() == 0) return;
		
		int drawIndex = thumbnailWidth/2 + 25;
		
		for (int i = 0, j = start_index; i < MAX_THUMBNAIL_DISPLAY && j < itemsForDisplay.size(); i++, j++){
			//This is the old way. Keeping it here just in case we need to revert
//			if( itemsForDisplay.get(j).checkType().equals("image")){
//				image = itemsForDisplay.get(j).getThumbnail();
//				
//			}
			//Get the thumbnail from the list
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
	
	/*
	 * Receiving the visual items
	 */
	public void receiveSelectedItems(ArrayList<VisualItem> selectedList){

		for(int i = oldListSize; i < selectedList.size(); i++){
			itemsForDisplay.add(selectedList.get(i));
		}
		oldListSize = selectedList.size();
	}
	
	/*
	 * 
	 * Clear the selected slides
	 */
	public void clearSelectedSlides(){
		itemsForDisplay.clear();
	}
	
	/**
	 * 
	 * to show next items
	 */
	public void showNextOnTimeline(){
		start_index += 8;
		if(start_index > itemsForDisplay.size()) start_index = 0;
	}
	
	/**
	 * 
	 * 
	 */
	
	public void showPrevOnTimeline(){
		start_index -= 8;
		if(start_index < 0) 
			start_index = itemsForDisplay.size() - (itemsForDisplay.size()%MAX_THUMBNAIL_DISPLAY);
	}
}
