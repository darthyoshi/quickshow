package quickshow;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Label;
import controlP5.Textarea;
import controlP5.Textfield;
import controlP5.Textlabel;
import controlP5.Toggle;

@SuppressWarnings("static-access")
public class controlbuttonUI {
    private Quickshow parent;
    
    private boolean debug;
    
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
	Button addNote, removeNote;
	Button[] lockButtons;
	Textfield pageIndex;
	private String indexString = "0 of 0";
	
	/*
	 * TODO make sure to get interactivity
	 */
	public controlbuttonUI(Quickshow parent, ControlP5 buttonUI){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
	    
		group = buttonUI.addGroup("buttonUI").setLabel("");
		
        lockButtons = new Button[14];
		
		//For the entire slideshow

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
	        //.setMode(ControlP5.SWITCH)
	        .setPosition(360, 10)
	        .setSize(20, 20)
	        .setCaptionLabel(" Shuffle Slides")
	        .setGroup(group);
		shuffleToggle.getCaptionLabel()
		    .align(buttonUI.RIGHT_OUTSIDE, buttonUI.CENTER);
		
		//For audioList
		lockButtons[3] = clearSongsButton = buttonUI
	        .addButton("Clear selected songs")
	        .setPosition(675, 402)
	        .setSize(193, 15)
	        .setGroup(group);
		lockButtons[3].getCaptionLabel().alignX(buttonUI.CENTER);
		
		//For the Thumbnail selector
		lockButtons[4] = selectAllImages = buttonUI.addButton("Select All Pictures")
		    .setPosition(140, 402)
		    .setSize(140,15)
		    .setGroup(group);
		lockButtons[4].getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[5] = selectAllVideos = buttonUI.addButton("Select All Clips")
		    .setPosition(290, 402)
		    .setSize(115, 15)
		    .setGroup(group);
		lockButtons[5].getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[6] = clearVisualTimeline = buttonUI
	        .addButton("Clear slides")
		    .setPosition(30, 402)
		    .setSize(100,15)
		    .setGroup(group);
		lockButtons[6].getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[7] = upButton = buttonUI.addButton("Up")
		    .setPosition(540, 402)
		    .setSize(50,15)
		    .setGroup(group);
		lockButtons[7].getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[8] = downButton = buttonUI.addButton("Down")
		    .setPosition(600, 402)
		    .setSize(50,15)
		    .setGroup(group);
		lockButtons[8].getCaptionLabel().alignX(buttonUI.CENTER);
		
		//To control the visual timeline thumbnail
		lockButtons[9] = nextSlides = buttonUI.addButton("Next")
		    .setPosition(802, 570)
		    .setGroup(group);
		lockButtons[9].getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[10] = prevSlides = buttonUI.addButton("Previous")
		    .setPosition(30, 570)
		    .setGroup(group);
		lockButtons[10].getCaptionLabel().alignX(buttonUI.CENTER);
		
		//Load media
		lockButtons[11] = loadMedia = buttonUI.addButton("Load Media")
		    .setPosition(675, 10)
		    .setSize(193, 15)
		    .setGroup(group);

		lockButtons[11].getCaptionLabel().alignX(buttonUI.CENTER);
		
		pageIndex = buttonUI.addTextfield("")
			.setPosition(440, 402)
			.setSize(75, 15)
			.setText(indexString)
			.setGroup(group);
		lockButtons[12] = addNote = buttonUI.addButton("Add Annotation")
	        .setPosition(295, 570)
	        .setSize(150, 15)
	        .setGroup(group);
		lockButtons[12].getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[13] = removeNote = buttonUI.addButton("Remove Annotation")
	        .setPosition(455, 570)
	        .setSize(150, 15)
	        .setGroup(group);
		lockButtons[13].getCaptionLabel().alignX(buttonUI.CENTER);
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
	
	/**
	 * 
	 * Sets the page indexing string for the label
	 */
	public void setPageIndex(int pages, int index){
		indexString = index + " of " + pages;
		pageIndex.setText(indexString);
	}
}
