package quickshow;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Toggle;

public class controlbuttonUI {
    private Quickshow parent;
    
    private boolean debug;
    
    private ControlP5 buttonUI;
    
    Group group;
	Button playButton;
	Button shareExportButton;
	Button resetShowButton;
	Button clearSongsButton;
	Button clearVisualTimeline;
	Button selectAllVideos;
	Button selectAllImages;
	Button upButton;
	Button downButton;
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
	public controlbuttonUI(Quickshow parent, ControlP5 buttonUI){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
	    
	    this.buttonUI = buttonUI;
	    
		group = buttonUI.addGroup("buttonUI").setLabel("");
		
        lockButtons = new Button[12];
		
		//For the entire slideshow

		//buttonUI.addListener(l);
		lockButtons[0] = playButton = buttonUI.addButton("Play")
	        .setPosition(30, 10)
	        .setGroup(group);
		playButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[1] = shareExportButton = buttonUI.addButton("Share/Export")
	        .setPosition(120, 10)
	        .setGroup(group)
	        .setSize(120, 20);
		shareExportButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		
		lockButtons[2] = resetShowButton = buttonUI.addButton("Reset")
	        .setPosition(270, 10)
	        .setGroup(group);
	        resetShowButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		shuffleToggle = buttonUI.addToggle("Shuffle Slides")
	        .setMode(ControlP5.SWITCH)
	        .setPosition(360, 10)
	        .setCaptionLabel("Shuffle Slides")
	        .setGroup(group);
		shuffleToggle.getCaptionLabel().alignY(buttonUI.RIGHT_OUTSIDE);
		
		//For audioList
		lockButtons[3] = clearSongsButton = buttonUI.addButton("Clear selected songs")
	        .setPosition(675, 402)
	        .setSize(193, 15)
	        .setGroup(group);
		
		//For the Thumbnail selector
		lockButtons[4] = selectAllImages = buttonUI.addButton("Select All Pictures")
		    .setPosition(200, 402)
		    .setSize(150,15)
		    .setGroup(group);
		
		lockButtons[5] = selectAllVideos = buttonUI.addButton("Select All Clips")
		    .setPosition(370, 402)
		    .setSize(150, 15)
		    .setGroup(group);
		
		lockButtons[6] = clearVisualTimeline = buttonUI.addButton("Clear slides")
		    .setPosition(30, 402)
		    .setSize(150,15)
		    .setGroup(group);
		
		lockButtons[7] = upButton = buttonUI.addButton("Up")
			    .setPosition(540, 402)
			    .setSize(50,15)
			    .setGroup(group);
		
		lockButtons[8] = downButton = buttonUI.addButton("Down")
			    .setPosition(600, 402)
			    .setSize(50,15)
			    .setGroup(group);
		
		//To control the visual timeline thumbnail
		lockButtons[9] = nextSlides = buttonUI.addButton("Next")
		    .setPosition(802, 570)
		    .setGroup(group);
		
		lockButtons[10] = prevSlides = buttonUI.addButton("Previous")
		    .setPosition(30, 570)
		    .setGroup(group);
		
		//Load media
		lockButtons[11] = loadMedia = buttonUI.addButton("Load Media")
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
        
        shuffleToggle.setLock(!visible);
	}
}
