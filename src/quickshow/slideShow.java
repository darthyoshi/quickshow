package quickshow;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.*;
import processing.video.*;
import quickshow.datatypes.*;
import ddf.minim.*;

public class slideShow {
	Quickshow parent;
	Minim minim;
    
	PImage curFrame;
	
	private ArrayList <AudioItem> audios;
	private ArrayList <VisualItem> visuals;
	Iterator<VisualItem> visualIter;
	Iterator<AudioItem> audioIter;
	AudioItem curAudioItem;
	VisualItem curVisualItem;
	
	private double imgDispTime;
	
	boolean isPlaying = false, isActive = false;
	
	public slideShow(Quickshow parent, Minim minim) {
		this.parent = parent;
		this.minim = minim;
		
		audios = new ArrayList<AudioItem>();
		visuals = new ArrayList<VisualItem>();
		
		curFrame = parent.createImage(parent.width, parent.height, parent.RGB);
	}
	
	public void addAudio(ArrayList<AudioItem>  newAudio) {
	    audios.clear();
	    audios.addAll(newAudio);
	    audioIter = audios.iterator();
	    curAudioItem = (audioIter.hasNext() ? audioIter.next() : null);
	}
	
	public void addVisual(ArrayList<VisualItem>  newVisual) {
	    visuals.clear();
        visuals.addAll(newVisual);
        visualIter = visuals.iterator();
        curVisualItem = (visualIter.hasNext() ? visualIter.next() : null);
        imgDispTime = 0;
    }
	
	public void updateAndDraw() {
	    if(isPlaying) {
	        if(curAudioItem != null) {
                if(curAudioItem.getAudio().position() ==
                    curAudioItem.getAudio().length())
                {
    	            if(audioIter.hasNext()) {
    	                curAudioItem = audioIter.next();
    	                curAudioItem.getAudio().play();
    	            }
    	        }
	        }
	        
	        if(curVisualItem != null) {
    	        if(curVisualItem.checkType().equals("image")) {
    	            imgDispTime += 0.04;
    	            
    	            if(imgDispTime >= 5.) {
    	                imgDispTime = 0.;
    	                
    	                nextVisualItem();
    	            }
    	        }
    	        
    	        else {
    	            
    	        }
	        }
	    
	        parent.image(curFrame, 0, 0);
	    }
	}
	
	//For playback functionality
	public void togglePlayMode(){
		isPlaying = !isPlaying;
	}
	
	private void nextVisualItem() {
	    if(visualIter.hasNext()) {
	        curVisualItem = visualIter.next();
	        
	        if(curVisualItem.checkType().equals("image")) {
	            
	        }
	    }
	}
	
	public void stop() {
	    isPlaying = false;
	    
	    curVisualItem = null;
	    curAudioItem = null;
	    
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
