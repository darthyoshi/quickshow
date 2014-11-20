package quickshow;

import java.util.ArrayList;

import processing.core.PImage;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.VisualItem;

public class visualTimeline {
	private final int timeLineWidth = 840;
	private final int timeLineHeight = 65;
	private final int thumbnailWidth = 104;
	float scaleFactor;
	ArrayList <VisualItem> itemsForDisplay;
    private Quickshow parent;
    PImage image;
    private int oldListSize;
    private boolean debug;
	
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
		//This is for a single item
		if(itemsForDisplay.size() == 0) {
			//System.out.println("No items in  selected visual items");
			return;
		}
		int drawIndex = thumbnailWidth/2 + 25;
		//TODO needs to be modified to handle multiple images
		for(VisualItem v: itemsForDisplay){
			if( v.checkType().equals("image")){
				image = ((ImageItem) v).getImage();
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
				drawIndex+= thumbnailWidth;
				if(drawIndex > timeLineWidth){
					//DO something
				}
			}
			
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
}
