/**
 * @file slideShow.java
 * @author Kay Choi, Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PImage;
import processing.video.Movie;
import quickshow.datatypes.AudioItem;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.MovieItem;
import quickshow.datatypes.VisualItem;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Group;
import ddf.minim.Minim;

@SuppressWarnings("static-access")
public class slideShow {
	private Quickshow parent;
	
	private boolean debug;
	
	private Minim minim;
	
	private Group group;
	private Button[] buttons;
    
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
		
		debug = parent.getDebugFlag();
		
		this.minim = minim;
		
		audios = new ArrayList<AudioItem>();
		visuals = new ArrayList<VisualItem>();
		
		group = control.addGroup("slideShow")
			.setCaptionLabel("")
			.setVisible(false);
		
		buttons = new Button[2];
		
		buttons[0] = control.addButton("playButton")
			.setLock(true)
			.setGroup(group);
		
		buttons[1] = control.addButton("stopButton")
			.setLock(true)
			.setGroup(group);
	}
	
	/**
	 * TODO add method header
	 * @param newAudio
	 */
	public void addAudio(ArrayList<AudioItem>  newAudio) {
	    audios.clear();
	    audios.addAll(newAudio);
	    
	    if(debug) {
	        parent.println("#audio items: " + audios.size());
	    }
	    
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
        
        if(debug) {
            parent.println("#visual items: " + visuals.size());
        }
        
        visualIter = visuals.iterator();
        //curVisualItem = (visualIter.hasNext() ? visualIter.next() : null);
        nextVisualItem();
    }
	
    /**
     * Callback method for handling ControlP5 UI events.
     * @param e the ControlEvent to handle
     */
	public void controlEvent(ControlEvent e) {
		switch(e.getName()) {
		case "playButton":
			togglePlayMode();
			
			break;
			
		case "stopButton":
			stop();
			
			break;
		}
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
    	            
    	            if(imgDispTime >= 2.) {
    	                imgDispTime = 0.;
    	                
    	                nextVisualItem();
    	            }
    	            
    	        }
    	        
    	        else {
    	            if(movie.available()) {
    	            	movie.read();
    	            }
    	            
    	            if(movie.time() == movie.duration()) {
    	                movie.stop();
    	                
    	                nextVisualItem();
    	            }
    	            
    	            curFrame = movie;
    	        }
	        }
	    }
	    
	    parent.imageMode(parent.CORNER);
        parent.image(curFrame, 0, 0);
	    
	    //TODO display UI elements
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
	    
	    if(curAudioItem != null) {
	        curAudioItem.getAudio().pause();
	    }
	    curAudioItem = null;

	    curVisualItem = null;
	    
        if(movie != null) {
            movie.stop();
            movie = null;
        }
        
	    toggle(false);
	    
	    parent.toggleMain(true);
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
	    group.setVisible(visible);
	    
	    for(Button button : buttons) {
	    	button.setLock(!visible);
	    }
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
	        if(debug) {
	            parent.println("starting audio file");
	        }
	        curAudioItem.getAudio().play();
	    }
	    
        imgDispTime = 0;
	}
	
}
