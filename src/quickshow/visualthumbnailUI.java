/**
 * @file visualthumbnailUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PShape;
import quickshow.datatypes.VisualItem;

import com.jogamp.opengl.util.packrect.Rect;

import controlP5.*;

public class visualthumbnailUI {
    private Quickshow parent;
    
    private ControlP5 control;
    
	int num_items;
	PShape rect1;
	ArrayList <VisualItem> items;
	ArrayList <VisualItem> selectedItems;
	
	final private int width = 620;
	final private int height = 370;
	
	public visualthumbnailUI(Quickshow parent, ControlP5 control){
	    this.parent = parent;
	    this.control = control;
	}
	
	/*
	 * 
	 */
	public void drawBackgroundCanvas(){
		parent.rect(30, 30, width, height);
	}
	
	/*
	 * 
	 */
	public void drawThumbNails(){
		
		//Iterate through the items to display them as thumbnail
		for (VisualItem v: items){
			
		}
	}
	
	/*
	 * Receive from the file browser
	 */
	public void receiveVisualItems(ArrayList <VisualItem> vItems){
		items.addAll(vItems);
	}
	
	public void toggle(boolean visible) {
	    
	}
}