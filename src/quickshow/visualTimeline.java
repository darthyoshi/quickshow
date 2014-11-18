package quickshow;

import java.util.Vector;

import quickshow.datatypes.VisualItem;

public class visualTimeline {
	private final int timeLineWidth = 840;
	private final int timeLineHeight = 65;
	Vector <VisualItem> itemsForDisplay;
    private Quickshow parent;
    private boolean debug;
	
	/*
	 * Empty Constructor need to some how do something
	 */
	public visualTimeline(Quickshow parent){
		this.parent = parent;
		
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
		
	}
}
