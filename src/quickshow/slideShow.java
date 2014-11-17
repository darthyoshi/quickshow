package quickshow;

import java.util.ArrayList;

import processing.video.*;
import quickshow.datatypes.*;

public class slideShow {
	//MovieMaker show;
	ArrayList <MediaItem> items;
	ArrayList <AudioItem> audio;
	ArrayList <ImageItem> images;
	ArrayList <MovieItem> movies;
	
	public slideShow(ArrayList<MediaItem> listOfItems){
		//Constructor
	}
	
	public void parseMediaItems(){
		//This will cause Null pointer exception for now because we have not
		//Instantiated these objects
		for (int i = 0; i < items.size(); i++){
			if(items.get(i).checkType().equals("audio")){
				audio.add((AudioItem) items.get(i));
			} else if (items.get(i).checkType().equals("image")){
				images.add((ImageItem) items.get(i));
			} else if (items.get(i).checkType().equals("video")){
				
			}
			
		}
	}
	
	public void generateShow(){
		
	}
	
	
	//For playback functionality
	public void pause(){
		
	}
	
	public void play(){
		
	}
	
	public void stop(){
		
	}
	
	/*
	 * Add helper functions below here
	 */
	
	
	
}
