package quickshow;

import java.util.ArrayList;
import java.util.Iterator;

import controlP5.ControlP5;
import processing.core.*;
import processing.video.*;
import quickshow.datatypes.*;
import ddf.minim.*;

public class slideShow {
	private Quickshow parent;
	private Minim minim;
	private ControlP5 control;
    
	private PImage curFrame;
	private Movie movie;
	
	private ArrayList <AudioItem> audios;
	private ArrayList <VisualItem> visuals;
	private Iterator<VisualItem> visualIter;
	private Iterator<AudioItem> audioIter;
	private AudioItem curAudioItem;
	private VisualItem curVisualItem;
	
	private double imgDispTime;
	
	private boolean isPlaying = false, isEnabled = false;
	
    /**
     * Class constructor.
     * @param parent
     * @param minim
     */
	public slideShow(Quickshow parent, Minim minim, ControlP5 control) {
		this.parent = parent;
		this.minim = minim;
		this.control = control;
		
		audios = new ArrayList<AudioItem>();
		visuals = new ArrayList<VisualItem>();
	}
	
	/**
	 * TODO add method header
	 * @param newAudio
	 */
	public void addAudio(ArrayList<AudioItem>  newAudio) {
	    audios.clear();
	    audios.addAll(newAudio);
	    audioIter = audios.iterator();
	    curAudioItem = (audioIter.hasNext() ? audioIter.next() : null);
	}
	
	/**
	 * TODO add method header
	 * @param newVisual
	 */
	public void addVisual(ArrayList<VisualItem>  newVisual) {
	    visuals.clear();
        visuals.addAll(newVisual);
        visualIter = visuals.iterator();
        curVisualItem = (visualIter.hasNext() ? visualIter.next() : null);
    }
	
	/**
	 * TODO add method header
	 */
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
    	            
    	            else {
    	                curAudioItem = null;
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
    	            if(movie.available()) {
    	                movie.read();
    	            }
    	            
    	            else if(movie.time() == movie.duration()) {
    	                movie.stop();
    	                
    	                nextVisualItem();
    	            }
    	            
    	            curFrame = movie;
    	        }
	        }
	    
	        parent.image(curFrame, 0, 0);
	    }
	}
	
	/**
	 * TODO add method header
	 */
	public void togglePlayMode(){
		isPlaying = !isPlaying;
		
		if(!isPlaying) {
		    curAudioItem.getAudio().pause();
		    
		    if(movie != null) {
		        movie.pause();
		    }
		}
		
		else {
		    curAudioItem.getAudio().play();
		    
		    if(movie != null) {
		        movie.play();
		    }
		}
	}
	
	/**
	 * TODO add method header
	 */
	private void nextVisualItem() {
	    movie = null;
	    curFrame = null;
	    
	    if(visualIter.hasNext()) {
	        curVisualItem = visualIter.next();
	        
	        if(curVisualItem.checkType().equals("video")) {
	            movie = ((MovieItem)curVisualItem).getMovie();
	            movie.play();
	        }
	        
	        else {
	            curFrame = ((ImageItem)curVisualItem).getImage();
	        }
	    }
	    
	    else {
	        stop();
	    }
	}
	
	/**
	 * TODO add method header
	 */
	public void stop() {
	    isPlaying = isEnabled = false;
	    
	    curAudioItem.getAudio().pause();
	    curAudioItem = null;

        curVisualItem = null;
        if(movie != null) {
            movie.stop();
            movie = null;
        }
        curFrame = null;
	    
	    toggle(false);
	}
	
	/**
	 * TODO add method header
	 * @return
	 */
	public boolean isPlaying() {
	    return isPlaying;
	}

	/**
	 * TODO toggle UI components
	 * @param visible
	 */
	public void toggle(boolean visible) {
	    
	}
	
	/**
	 * TODO add method header
	 * @return
	 */
	public boolean isEnabled() {
	    return isEnabled;
	}
	
	/**
	 * TODO add method header
	 */
	public void startPlaying() {
	    isPlaying = isEnabled = true;
	    
	    if(curAudioItem != null) {
	        curAudioItem.getAudio().play();
	    }
	    
        imgDispTime = 0;
	}
	
}
