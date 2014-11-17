package quickshow;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Toggle;

public class controlbuttonUI {
    Group group;
	Button playButton;
	Button shareExportButton;
	Button resetShowButton;
	Button clearSongsButton;
	Button clearVisualTimeline;
	Button selectAllVideos;
	Button selectAllImages;
	Toggle shuffleToggle;
	Button nextSlides;
	Button prevSlides;
	Button loadMedia;
	Button[] lockButtons;
	
	ControlEvent events;
	ControlListener l;
	
	/*
	 * TODO make sure to get interactivity
	 */
	public controlbuttonUI(ControlP5 buttonUI){
		group = buttonUI.addGroup("buttonUI").setLabel("");
		
        lockButtons = new Button[10];
		
		//For the entire slideshow

		//buttonUI.addListener(l);
		lockButtons[0] = playButton = buttonUI.addButton("Play")
	        .setPosition(30, 10)
	        .setGroup(group);
		playButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[1] = shareExportButton = buttonUI.addButton("Share/Export")
	        .setPosition(120, 10)
	        .setGroup(group);
		
		lockButtons[2] = resetShowButton = buttonUI.addButton("Reset")
	        .setPosition(210, 10)
	        .setGroup(group);
		
		shuffleToggle = buttonUI.addToggle("Shuffle Slides")
	        .setMode(ControlP5.SWITCH)
	        .setPosition(300, 10)
	        .setCaptionLabel("Shuffle Slides")
	        .setGroup(group);
		
		//For audioList
		lockButtons[3] = clearSongsButton = buttonUI.addButton("Clear selected songs")
	        .setPosition(675, 400)
	        .setSize(193, 15)
	        .setGroup(group);
		
		//For the Thumbnail selector
		lockButtons[4] = selectAllImages = buttonUI.addButton("Select All Pictures")
		    .setPosition(200, 400)
		    .setSize(150,15)
		    .setGroup(group);
		
		lockButtons[5] = selectAllVideos = buttonUI.addButton("Select All Clips")
		    .setPosition(370, 400)
		    .setSize(150, 15)
		    .setGroup(group);
		
		lockButtons[6] = clearVisualTimeline = buttonUI.addButton("Clear slides")
		    .setPosition(30, 400)
		    .setSize(150,15)
		    .setGroup(group);
		
		//To control the visual timeline thumbnail
		lockButtons[7] = nextSlides = buttonUI.addButton("Next")
		    .setPosition(805, 570)
		    .setGroup(group);
		
		lockButtons[8] = prevSlides = buttonUI.addButton("Previous")
		    .setPosition(30, 570)
		    .setGroup(group);
		
		//Load media
		lockButtons[9] = loadMedia = buttonUI.addButton("Load Media")
		    .setPosition(675, 10)
		    .setSize(193, 15)
		    .setGroup(group);
	}
	
	/**
	 * Toggles the visibility of the UI elements.
	 * @param visible whether or not the UI elements should be visible
	 */
	public void toggle(boolean visible) {
	    group.setVisible(visible);
	    
	    for(Button button : lockButtons) {
	        button.setLock(!visible);
	    }
	}
	
	/**
	 * 
	 * 
	 */
	public void controlEvent(ControlEvent e, Quickshow q){
		String srcName = e.getLabel();
    	switch(srcName){
    	case "Play": 
    		break;
    	case "Share/Export": 
    		break;
    	case "Reset":
    		break;
    	case "Shuffle Slides": 
    		break;
    	case "Clear selected songs": 
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
    		
    		q.browse.toggle(true);
    		toggle(false);
    		q.aT.toggle(false);
    		q.audioListbox.toggle(false);
    		break;
    	}
	}
}
