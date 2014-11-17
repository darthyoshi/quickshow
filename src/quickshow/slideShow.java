package quickshow;

import java.util.ArrayList;

import processing.video.*;
import quickshow.datatypes.*;

public class slideShow {
	Quickshow parent;
    
	ArrayList <AudioItem> audios;
	ArrayList <VisualItem> visuals;
	
	boolean isPlaying = false;
	
	public slideShow(Quickshow parent) {
		this.parent = parent;
		
		audios = new ArrayList<AudioItem>();
		visuals = new ArrayList<VisualItem>();
	}
	
	public void addAudio(ArrayList<AudioItem>  newAudio) {
	    audios.clear();
	    audios.addAll(newAudio);
	}
	
	public void addVisual(ArrayList<VisualItem>  newVisual) {
	    visuals.clear();
        visuals.addAll(newVisual);
    }
	
	public void draw() {
	    
	}
	
	//For playback functionality
	public void togglePlayMode(){
		isPlaying = !isPlaying;
	}
	
	public void stop() {
		
	}
	
	public boolean isPlaying() {
	    return isPlaying;
	}
	
	/*
	 * Add helper functions below here
	 */
	
	
	
}
