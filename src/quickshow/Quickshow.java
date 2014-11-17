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
//	controlbuttonUI cbU;
	FileBrowser browse;
	
	//Test variables for debug purposes
/*	audioTimeline aT;
	visualTimeline vT;
	visualthumbnailUI vThumb;
	*/
	public void setup() {
		setSize(900, 600);
/*		audioList = new ControlP5(this);
		audioListbox = new audiolistUI(audioList);
		
		buttons = new ControlP5(this);
		cbU = new controlbuttonUI(buttons);
		
		
		//Test purposes delete/modify this after
		aT = new audioTimeline(this);
		aT.generateWaveForm();
		
		vT = new visualTimeline();
		
		vThumb = new visualthumbnailUI();
		//Test purposes delete lines above
*/	
		minim = new Minim(this);
		
		browse = new FileBrowser(this, minim, ".");
		browse.toggle(true);
	}

	public void draw() {
	    background(0xaaaaaa);
	    
	    if(browse.isEnabled()) {
	        browse.draw();
	    }
	    
	    else {
			//Background for the thumbnails
/*    	    aT.drawBackgroundCanvas(this);
    	    aT.drawWaveform(this);
    	    vT.drawBackgroundCanvas(this);
    	    vThumb.drawBackgroundCanvas(this);
*/
	    }

	}
	
	/**
	 * Callback method for handling ControlP5 UI events.
	 * @param theEvent the initiating ControlEvent
	 */
	public void controlEvent(ControlEvent theEvent) {
	    if(
            browse.isEnabled() &&
            (theEvent.isController() &&
            theEvent.getController().getParent().getName()
                .equals("fileBrowser")) || 
            (theEvent.isGroup() &&
            theEvent.getGroup().getParent().getName().equals("fileBrowser"))
        ) {
            browse.controlEvent(theEvent);
                
            if(!browse.isEnabled() && browse.isReady()) {
                ArrayList<MediaItem> results = browse.getResults();
                
                if(debug) {
                    println(results.size());
                }
                //TODO pass results to media player
            }
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