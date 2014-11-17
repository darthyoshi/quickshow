/**
 * @file Quickshow.java
 * @author Kay Choi, Moses Lee
 * @description The main Quickshow class. Functions as a hub for all other
 *   application components. 
 */

package quickshow;

import processing.core.*;
import quickshow.datatypes.*;
import controlP5.*;
import ddf.minim.*;

import java.util.*;

@SuppressWarnings("serial")
public class Quickshow extends PApplet {
    boolean debug = true;
    
	ControlP5 audioList;
	audiolistUI audioListbox;
	
	Minim minim;
	
	ControlP5 visualThumbnail;
	visualthumbnailUI thumbnails;
	ArrayList <AudioItem> audioFiles;

	ControlP5 buttons;
	controlbuttonUI cbU;
	FileBrowser browse;
	slideShow show;
	
	//Test variables for debug purposes
	audioTimeline aT;
	visualTimeline vTimeline;
	visualthumbnailUI vThumb;
	
	
	public void setup() {
		setSize(900, 600);
		audioList = new ControlP5(this);
		audioListbox = new audiolistUI(audioList);
		
		buttons = new ControlP5(this);
		cbU = new controlbuttonUI(buttons);
		
		//Test purposes delete/modify this after
		aT = new audioTimeline(this);
		aT.generateWaveForm();
		
		vTimeline = new visualTimeline();
		thumbnails = new visualthumbnailUI();
		//Test purposes delete lines above
	
		minim = new Minim(this);
		
		browse = new FileBrowser(this, minim, ".");
	}

	public void draw() {
	    background(38, 38, 38);
	    
	    if(browse.isEnabled()) {
	        browse.draw();
	    }
	    
	    else {
			//Background for the thumbnails
	    	stroke(0,0,0);
	    	fill(90,90,90);
	    	rectMode(CORNER);
	    	aT.drawBackgroundCanvas(this);
    	    vTimeline.drawBackgroundCanvas(this);
    	    thumbnails.drawBackgroundCanvas(this);
    	    
	    	//This line is a place holder
    	    aT.drawWaveform(this);
    	    
	    }

	}
	
	public void update(){
		
	}
	
	/**
	 * Callback method for handling ControlP5 UI events.
	 * @param theEvent the initiating ControlEvent
	 */
	public void controlEvent(ControlEvent theEvent) {
	    String srcName = "";
	    if(theEvent.isController()) {
	        srcName = theEvent.getController().getParent().getName();
	    }
	    
	    else if (theEvent.isGroup()) {
	        srcName = theEvent.getGroup().getParent().getName();
	    }
	    
	    println("in control Event == " + srcName);
	    
	    switch(srcName) {
	    case "fileBrowser":
	        if(browse.isEnabled()) {
            browse.controlEvent(theEvent, this);
                
                if(!browse.isEnabled() && browse.isReady()) {
                    ArrayList<MediaItem> results = browse.getResults();
                    
                    if(debug) {
                        println("RESULT SIZE " + results.size());
                    }
                    
                    if(browse.isAudioMode()) {
                        ArrayList<AudioItem> audios = new ArrayList<AudioItem>();
                        
                        for(MediaItem item : results) {
                            audios.add((AudioItem)item);
                        }
                        
                        audioListbox.receiveSongs(audios);
                    }
                    
                    else {
                        ArrayList<VisualItem> visuals = new ArrayList<VisualItem>();
                        
                        for(MediaItem item : results) {
                            visuals.add((VisualItem)item);
                        }
                        
                        thumbnails.receiveVisualItems(visuals);
                    }
                    cbU.toggle(true);
                    audioListbox.toggle(true);
                }
	        }
	        break;
	        /*
	         * TODO Need to implement a way to get this interfacing with the classes
	         */
	    case "buttonUI":
	    	srcName = theEvent.getLabel();
	    	println(srcName);
	    	cbU.controlEvent(theEvent, this);
	        break;
	        
	    case "AudioList":
	    	int test = theEvent.getId();
	    	println(srcName + test);
	    }
    }
	 
	public void mouseClicked() {
	    if(browse.isEnabled()) {
	        browse.mouseClicked(mouseX, mouseY);
	    }
        
    }
    
    public void mouseDragged() {
        if(browse.isEnabled()) {
            browse.mouseDragged(mouseX, mouseY);
        }
    }
    
    public void mouseReleased() {
        if(browse.isEnabled()) {
            browse.mouseReleased(mouseX, mouseY);
        }
    }
    
    public void mousePressed() {
        if(browse.isEnabled()) {
            browse.mousePressed(mouseX, mouseY);
        }
    }
    
    /**
     * Main method for executing Quickshow as a Java application. 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", "Quickshow" });
    }
}