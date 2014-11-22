/**
 * @file visualthumbnailUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;

import processing.core.PImage;
import processing.core.PShape;
import processing.video.Movie;
import quickshow.datatypes.*;
import controlP5.*;

public class visualthumbnailUI {
    private Quickshow parent;

    private boolean debug = true;
    
	int num_items;
	PImage p;
	Movie movie;
	ArrayList <VisualItem> items;
	ArrayList <VisualItem> selectedItems;
	
	final private int MAX_THUMBNAIL_HEIGHT = 124;
	final private int MAX_THUMBNAIL_WIDTH = 123;
	final private int MAX_NUM_DISPLAY = 15;
	final private int width = 620;
	final private int height = 370;
	
	private int start_index = 0;
	private int num_pages;
	
	private float my_new_height;
	private float my_new_width;
	final int lowXBound = 30;
	final int highXBound = 650;
	final int lowYBound = 30;
	final int highYBound = 400;

	private int oldListSize = 0;
	
	

	public visualthumbnailUI(Quickshow parent){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
	    items = new ArrayList<VisualItem>();
	    selectedItems = new ArrayList<VisualItem>();
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
		int xStartIndex = MAX_THUMBNAIL_WIDTH/2 + 30;
		int yStartIndex = MAX_THUMBNAIL_HEIGHT/2 + 30;
		float scaleFactor;
		
		if(items.size() > 0) {
		//Iterate through the items to display them as thumbnail
			scaleFactor = 1.0f;
			for (int i = 0, j = start_index; i < MAX_NUM_DISPLAY && j < items.size(); i++, j++){
				
				if(items.get(j).checkType().equals("image")) {
					p = ((ImageItem) items.get(j)).getImage();
					if (p.height > MAX_THUMBNAIL_HEIGHT || p.width > MAX_THUMBNAIL_WIDTH){
						if(p.height >= p.width){
							scaleFactor = 1.0f/((float) p.height/ (float) (MAX_THUMBNAIL_HEIGHT-15));
						}
						else {
							scaleFactor = 1.0f/((float) p.width/ (float) (MAX_THUMBNAIL_WIDTH-15));
						}
					}
					
					my_new_height = (float) p.height * scaleFactor;
					my_new_width = (float) p.width * scaleFactor;
					
					parent.image(p, xStartIndex, yStartIndex, my_new_width, my_new_height);
					
				}
				if(items.get(j).checkType().equals("video")){
					//Generate thumbnail
					movie = ((MovieItem) items.get(j)).getMovie();
					movie.play();
					System.out.println("Generating movie thumbnail");
					//Read the movie
                    p.copy(movie, 0, 0, movie.width, movie.height, 0, 0, MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_HEIGHT);
                    movie.stop();
                    
                    parent.image(p, xStartIndex, yStartIndex, MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_HEIGHT);
				}
				
				//Move up the next index whether its video or image
				xStartIndex += MAX_THUMBNAIL_WIDTH;
				if(xStartIndex > width) {
					if(yStartIndex < height){
						yStartIndex += MAX_THUMBNAIL_HEIGHT;
						xStartIndex = MAX_THUMBNAIL_WIDTH/2 + 30;
					}
				}
			}
		}
	}
	
	/*
	 * Receive from the file browser
	 */
	public void receiveVisualItems(ArrayList <VisualItem> vItems){
		System.out.println("Receiving items size: " + vItems.size());
		
		
		for(int i = oldListSize; i < vItems.size(); i++){
			items.add(vItems.get(i));
		}
		oldListSize = vItems.size();
		
		num_pages = items.size()/MAX_NUM_DISPLAY;
	}
	
	/*
	 * 
	 */
	public void selectImage(int x, int y){
		System.out.println("X: " + x + " Y: " + y);
		
		int xValue = x - lowXBound;
		int yValue = y - lowYBound;
		
		int xIndex = xValue/124;
		int yIndex = yValue/123;
		int mainIndex = start_index + ((yIndex * 5) + xIndex);
		
		System.out.println("Grid coord x: " + xIndex + " y: " + yIndex);
		System.out.println("main index: " + mainIndex);
		
//		//Make sure we are in legal range
		if(mainIndex < items.size()){
			selectedItems.add(items.get(mainIndex));
			
			if(debug) {
			    //parent.println("Added image: " + selectedItems.get(mainIndex).checkType());
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public ArrayList<VisualItem> returnSelectedItems(){
		return selectedItems;
	}
	
	/**
	 * TODO for demo - retrieves all visualItems
	 * @return
	 */
	public ArrayList<VisualItem> getVisualItems() {
	    return items;
	}
	
	/**
	 * Goes down one page
	 * 
	 */
	public void showNextItems(){
		start_index += 15;
		if(start_index > items.size()){
			start_index = 0;
		}
	}
	
	/**
	 * Goes up one page
	 * 
	 */
	
	public void showPrevItems(){
		start_index -= 15;
		if(start_index < 0){
			start_index = items.size() - (items.size()%15);
		}
	}
}