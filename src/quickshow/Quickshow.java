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
    private boolean debug = true;
    
	ControlP5 control;
	audiolistUI audioListbox;
	
	Minim minim;
	
	visualthumbnailUI thumbnails;
	ArrayList <AudioItem> audioFiles;

	controlbuttonUI cbU;
	FileBrowser browse;
	slideShow show;
	
	//Test variables for debug purposes
	audioTimeline aT;
	visualTimeline vTimeline;
	
	//These variables are for the Visual Thumbnail UI to bound where the mouse responds
	final int lowXBound = 30;
	final int highXBound = 650;
	final int lowYBound = 30;
	final int highYBound = 400;
	
	public void setup() {
		setSize(900, 600);
		frameRate(25);

		control = new ControlP5(this);
        control.setFont(control.getFont().getFont(), 15);
        
		minim = new Minim(this);
		
		show = new slideShow(this, minim, control);
		
		audioListbox = new audiolistUI(this, control);
		
		cbU = new controlbuttonUI(this, control);
		
		//Test purposes delete/modify this after
		aT = new audioTimeline(this, minim);
		aT.generateWaveForm();
		
		vTimeline = new visualTimeline(this);
		thumbnails = new visualthumbnailUI(this);
		//Test purposes delete/modify lines above
		
		browse = new FileBrowser(this, minim, control, ".");
	}

	public void draw() {
	    background(38, 38, 38);
	    
	    if(browse.isEnabled()) {
	        browse.draw();
	    }
	    else if(show.isEnabled()){
	    	show.updateAndDraw();
	    }
	    
	    else {
			//Background for the thumbnails
	    	stroke(0,0,0);
	    	fill(90,90,90);
	    	rectMode(CORNER);
	    	aT.drawBackgroundCanvas();
    	    vTimeline.drawBackgroundCanvas();
    	    thumbnails.drawBackgroundCanvas();
    	    
	    	//This line is a place holder
    	    aT.generateWaveForm();
    	    aT.drawWaveform();
			thumbnails.drawThumbNails();
			vTimeline.generateThumbnails();
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
	            browse.controlEvent(theEvent);
                
                if(!browse.isEnabled()) {
                    closeFBActions();
                }
	        }
	        break;
	        
	    case "buttonUI":
	        switch(theEvent.getName()){
	        case "Play":
	            toggleMain(false);
	            
	            show.addAudio(audioListbox.getAudioItems());
	            show.addVisual(thumbnails.getVisualItems());
	        	show.startPlaying();
	        	
	            break;
	        
	        case "Share/Export": 
	            break;
	        
	        case "Reset":
	            break;
	        
	        case "Shuffle Slides": 
	            break;
	        
	        case "Clear selected songs": 
	        	audioListbox.clearSelectedSongs();
	            break;
	        
	        case "Select All Pictures": 
	            break;
	        
	        case "Select All Clips": 
	            break;
	        
	        case "Clear slides": 
	            break;
	        
	        case "Next": 
	            break;
	        
	        case "Previous": 
	            break;
	        
	        case "Load Media":
	            browse.toggle(true);
	            toggleMain(false);
	            break;
	        }
	        break;
	        
	    case "AudioList":
	    	int test = theEvent.getId();
	    	float value = theEvent.getGroup().getValue();
	    	String testStr = theEvent.getGroup().getAddress();
	    	println(srcName + test + testStr+value);
	    	audioListbox.addToSelectedSongs((int) value);
	    	aT.receiveSelectedSongs(audioListbox.sendSongList());
	    }
    }
	 
	public void mouseClicked() {
	    if(browse.isEnabled()) {
	        browse.mouseClicked(mouseX, mouseY);
	        
	        if(!browse.isEnabled()) {
	            closeFBActions();
	        }
	    }
	    else {
	    	if(mouseX > lowXBound && mouseX < highXBound && mouseY > lowYBound && mouseY < highYBound){
	    		thumbnails.selectImage(mouseX, mouseY);
	    		vTimeline.receiveSelectedItems(thumbnails.returnSelectedItems());
	    	}
	    	
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
     * TODO method header
     */
    private void closeFBActions() {
        if(browse.isReady()) {
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
        }

        toggleMain(true);
    }

    /**
     * 
     * @param visible
     */
    public void toggleMain(boolean visible) {
        cbU.toggle(visible);
        aT.toggle(visible);
        audioListbox.toggle(visible);
    }
    
    public boolean getDebugFlag() {
        return debug;
    }
    
    /**
     * Main method for executing Quickshow as a Java application. 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", "Quickshow" });
    }
}