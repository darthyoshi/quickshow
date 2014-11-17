package quickshow;

import java.util.ArrayList;

import processing.video.*;
import quickshow.datatypes.*;
import ddf.minim.*;

public class slideShow {
	Quickshow parent;
	Minim minim;
    
	ArrayList <AudioItem> audios;
	ArrayList <VisualItem> visuals;
	
	boolean isPlaying = false, isActive = false;
	
	public slideShow(Quickshow parent, Minim minim) {
		this.parent = parent;
		this.minim = minim;
		
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
	    if(isPlaying) {
	        
	    }
	}
	
	//For playback functionality
	public void togglePlayMode(){
		isPlaying = !isPlaying;
	}
	
	public void stop() {
	    isPlaying = false;
	    
	    toggle(false);
	}
	
	public boolean isPlaying() {
	    return isPlaying;
	}

	public void toggle(boolean visible) {
	    
	}
	/*
	 * Add helper functions below here
	 */
	
	public boolean isEnabled() {
	    return false;
	}
	
}
