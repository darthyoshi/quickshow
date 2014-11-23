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
import controlP5.Toggle;
import ddf.minim.Minim;

@SuppressWarnings("static-access")
public class slideShow {
	private Quickshow parent;
	
	private boolean debug;
	
	private Minim minim;
	
	private Group group;
	private Button stopButton;
	private Toggle playToggle;

	private PImage curFrame;
	private Movie movie;
	
	private int frameWidth, frameHeight;
	
	private ArrayList<AudioItem> audios;
	private ArrayList<VisualItem> visuals;
	private Iterator<VisualItem> visualIter = null;
	private Iterator<AudioItem> audioIter = null;
	private AudioItem curAudioItem = null;
	private VisualItem curVisualItem = null;
	
	private ArrayList<String> curAnnotationTexts;
	private ArrayList<Float[]> curAnnotationTimes;
	
	private double imgDispTime;
	
	private boolean isPlaying = false, isEnabled = false, shuffle = false;
	
	private int shuffleIndex;
	
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
		
		curAnnotationTexts = new ArrayList<String>();
		curAnnotationTimes = new ArrayList<Float[]>();
		
		group = control.addGroup("slideShow")
			.setCaptionLabel("")
			.setVisible(false);
		
		playToggle = control.addToggle("playToggle")
	        .setCaptionLabel("")
	        .setPosition(10, 10)
	        .setSize(30, 30)
	        .setGroup(group)
	        .setImages(
                parent.loadImage("data/img/playbutton.png"),
                null,
                parent.loadImage("data/img/pausebutton.png"),
                null
            ).setLock(true);

		stopButton = control.addButton("stopButton")
	        .setCaptionLabel("")
			.setLock(true)
			.setPosition(50, 10)
			.setSize(30, 30)
			.setImage(parent.loadImage("data/img/stopbutton.png"))
			.setGroup(group);
		
		curFrame = parent.createImage(0, 0, parent.RGB);
	}
	
	/**
	 * Populates the audio component of the slide show.
	 * @param newAudio an ArrayList containing the new AudioItems
	 */
	public void addAudio(ArrayList<AudioItem>  newAudio) {
	    audios.clear();
	    audios.addAll(newAudio);
	    
	    if(debug) {
	        parent.println("#audio items: " + audios.size());
	    }
	    
	    if(!shuffle) {
	        audioIter = audios.iterator();
	    }
	    
	    nextAudioItem();
	}
	
	/**
	 * Populates the visual component of the slide show.
	 * @param newVisual an ArrayList containing the new VisualItems
	 */
	public void addVisual(ArrayList<VisualItem>  newVisual) {
	    visuals.clear();
        visuals.addAll(newVisual);
        
        if(debug) {
            parent.println("#visual items: " + visuals.size());
        }
        
        if(!shuffle) {
            visualIter = visuals.iterator();
        }

        nextVisualItem();
    }
	
    /**
     * Callback method for handling ControlP5 UI events.
     * @param e the ControlEvent to handle
     */
	public void controlEvent(ControlEvent e) {
		switch(e.getName()) {
		case "playToggle":
			playToggle(playToggle.getState());
			
			break;
			
		case "stopButton":
			stopButton();
			
			break;
		}
	}
	
	/**
	 * Callback method for displaying the slide show.
	 */
	public void draw() {
	    parent.background(0xff555555);
	    
	    if(isPlaying) {
	        if(curAudioItem != null) {
                if(curAudioItem.getAudio().position() ==
                    curAudioItem.getAudio().length())
                {
    	            nextAudioItem();
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
    	            
    	            curFrame = movie;
    	            
    	            if(movie.time() == movie.duration()) {
    	                movie.stop();
    	                
    	                nextVisualItem();
    	            }
    	            
    	            if(frameWidth == 0){
    	                calcFrameDims();
    	            }
    	            
    	            curFrame.resize(frameWidth, frameHeight);
    	        }
	        }
	        
	        else {
	        	stopButton();
	        }
	    }
	    
	    parent.imageMode(parent.CENTER);
        parent.image(curFrame, parent.width/2, parent.height/2);
	}
	
	/**
	 * ControlP5 UI handler. Pauses and resumes slide show playback.
	 * @param mode the new playback mode
	 */
	public void playToggle(boolean mode){
		isPlaying = mode;
		
		if(debug) {
		    parent.println("playing: " + mode);
		}

		if(!isPlaying) {
		    if(curAudioItem != null) {
		        curAudioItem.getAudio().pause();
		    }
		    
		    if(movie != null) {
		        movie.pause();
		    }
		}
		
		else {
		    if(curAudioItem != null) {
		        curAudioItem.getAudio().play();
		    }

		    if(movie != null) {
		        movie.play();
		    }
		}
	}
	
	/**
	 * Calculates the dimensions of the VisualItem frame.
	 */
	private void calcFrameDims() {
        float aspect = 1f * curFrame.width / curFrame.height;
        
        frameWidth = (curFrame.width > parent.width ?
            parent.width : curFrame.width);
        frameHeight = (int)(frameWidth / aspect);
        
        if(frameHeight > parent.height) {
            frameHeight = parent.height;
            frameWidth = (int)(frameHeight * aspect);
        }
	}
	
	/**
	 * Prepares the next AudioItem in the playlist.
	 */
	private void nextAudioItem() {
	    if(shuffle && !audios.isEmpty()) {
	        int shuffleIndex = (int)(Math.random()*audios.size());
	        
	        curAudioItem = audios.get(shuffleIndex);
	        
	        audios.remove(shuffleIndex);
	    }
	    
	    else if(audioIter != null && audioIter.hasNext()) {
	        curAudioItem = audioIter.next();
	    }
	    
	    else {
	        curAudioItem = null;
	    }
	}
	
	/**
	 * Prepares the next VisualItem in the playlist.
	 */
	private void nextVisualItem() {
	    movie = null;

	    frameWidth = frameHeight = 0;
	    
	    if(shuffle && !visuals.isEmpty()) {
	        int shuffleIndex = (int)(Math.random()*visuals.size());
	    
	        curVisualItem = visuals.get(shuffleIndex);
	        
	        visuals.remove(shuffleIndex);
	    }
	    
	    else if(visualIter != null && visualIter.hasNext()) {
	        curVisualItem = visualIter.next();
	    }
	    
	    else {
	        stopButton();
	    }
        
        curAnnotationTexts.addAll(curVisualItem.getAnnotationTexts());
        curAnnotationTimes.addAll(curVisualItem.getAnnotationTimes());

        //TODO figure out which tags should display and when
        
        if(curVisualItem.checkType().equals("video")) {
            movie = ((MovieItem)curVisualItem).getMovie();
            movie.play();
        }
        
        else {
            curFrame = ((ImageItem)curVisualItem).getImage();
            
            calcFrameDims();
            
            curFrame.resize(frameWidth, frameHeight);
        }
	}
	
	/**
	 * ControlP5 UI handler. Stops slide show playback.
	 */
	public void stopButton() {
	    isEnabled = false;
	    
	    playToggle.setState(isEnabled);
	    
	    audioIter = null;
	    if(curAudioItem != null) {
	        curAudioItem.getAudio().pause();
	    }
	    curAudioItem = null;

	    visualIter = null;
	    curVisualItem = null;
	    
        if(movie != null) {
            movie.stop();
            movie = null;
        }
        
	    toggleUI(false);
	    
	    parent.toggleMain(true);
	}
	
	/**
	 * Retrieves the current play mode of the slide show.
	 * @return true if the slide show is playing
	 */
	public boolean isPlaying() {
	    return isPlaying;
	}

	/**
	 * Sets the visibility of the slide show UI components.
	 * @param visible the visibility state
	 */
	private void toggleUI(boolean visible) {
	    group.setVisible(visible);
	    
	    playToggle.setLock(!visible);
	    
	    stopButton.setLock(!visible);
	}
	
	/**
	 * Retrieves the current state of the slide show.
	 * @return true if the slide show is active
	 */
	public boolean isEnabled() {
	    return isEnabled;
	}
	
	/**
	 * Enables the slide show and begins playback.
	 */
	public void startPlaying() {
	    isEnabled = true;
	    
	    playToggle.setState(isEnabled);
	    
	    if(movie != null) {
	    	movie.play();
	    }
	    
	    if(curAudioItem != null) {
	        if(debug) {
	            parent.println("starting audio file");
	        }
	        curAudioItem.getAudio().play();
	    }
	    
	    toggleUI(true);
	    
        imgDispTime = 0;
	}
	
	/**
	 * Toggles the slide show shuffle.
	 * @param shuffle whether or not to shuffle the slide show
	 */
	public void toggleShuffle(boolean shuffle) {
	    this.shuffle = shuffle;
	}
}
