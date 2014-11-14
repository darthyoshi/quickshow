package quickshow;

import processing.core.*;
import quickshow.datatypes.*;
import controlP5.*;

import java.util.*;

@SuppressWarnings("serial")
public class Quickshow extends PApplet {
	ControlP5 audioList;
	audiolistUI audioListbox;
	
	ControlP5 visualThumbnail;
	visualthumbnailUI thumbnails;
	ArrayList <AudioItem> audioFiles;
	
	FileBrowser browse;
	
	//Test variables for debug purposes
	audioTimeline aT;
	visualTimeline vT;
	
	public void setup() {
		setSize(900, 600);
		audioList = new ControlP5(this);
		audioListbox = new audiolistUI(audioList);
		visualThumbnail = new ControlP5(this);
		
		//Test purposes delete this after
		aT = new audioTimeline(this);
		aT.generateWaveForm();
		rect(30, 30, 620, 370);
		
		vT = new visualTimeline();
		//Test purposes delete lines above
		
		//browse = new FileBrowser(this, ".");
		//browse.toggle(true, false);
	}

	public void draw() {
	    background(0xaaaaaa);
	    
//	    if(browse.isEnabled()) {
//	        browse.draw();
//	    }
//	    
//	    else {
//			//Background for the thumbnails
//    		rect(30, 30, 620, 375);
//    		fill(255,0,0);
//    		
//    		aT.drawWaveform(this);
//	    }
	    rect(30, 30, 620, 370);
	    aT.drawBackgroundCanvas(this);
	    aT.drawWaveform(this);
	    vT.drawBackgroundCanvas(this);
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
}