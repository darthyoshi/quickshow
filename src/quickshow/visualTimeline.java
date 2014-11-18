package quickshow;

import java.util.ArrayList;

import processing.core.PImage;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.VisualItem;

public class visualTimeline {
	private final int timeLineWidth = 840;
	private final int timeLineHeight = 65;
	ArrayList <VisualItem> itemsForDisplay;
    private Quickshow parent;
    PImage image;
	
	/*
	 * Empty Constructor need to some how do something
	 */
	public visualTimeline(Quickshow parent){
		this.parent = parent;
		itemsForDisplay = new ArrayList<VisualItem>();
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
		for(VisualItem v: itemsForDisplay){
			if( v.checkType().equals("image")){
				image = ((ImageItem) v).getImage();
				parent.image(image, 70, 500, 65 ,65);
			}
			
		}
	}
	
	/*
	 * Receiving the visual items
	 */
	public void receiveSelectedItems(ArrayList<VisualItem> selectedList){
		itemsForDisplay.addAll(selectedList);
	}
}
