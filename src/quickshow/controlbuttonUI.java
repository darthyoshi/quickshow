/**
 * @file controlbuttonUI.java
 * @author Moses Lee, Kay Choi
 * @description A handler class for the main Quickshow window UI controls. 
 */

package quickshow;

import quickshow.datatypes.AudioItem;
import controlP5.Button;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.Group;
import controlP5.Slider;
import controlP5.Textfield;
import controlP5.Toggle;

public class controlbuttonUI {
    private Quickshow parent;
    
    private boolean debug;
    
    private Group mainUIGroup, popupGroup;
    private Button playButton;
    private Button resetShowButton;
    private Button clearSongsButton;
    private Button clearVisualTimeline;
    private Button selectAllVideos;
    private Button selectAllImages;
    private Button upButton;
    private Button downButton;
    private Toggle shuffleToggle;
    private Button nextSlides;
	private Button prevSlides;
	private Button loadMedia;
	private Button editVisualItem;
	private Button[] lockButtons;
	private Button pageIndex;
	private Button timeLineIndex;
	private Button songTitle;
	private Button songIndex;
	private Button nextSong;
	private Button prevSong;
	private Button totalTime;
	
	private static final int[] popupOrigin = {400, 300};
	private Slider imgDisplaySlider;
	private Button popupAccept, popupCancel;
	private Textfield tagField;
	
	private String songTitleString = "Song: ";
	private String indexString = "0 of 0";
	private String timeLineIndexString = "0:00 - 0:00";
	private String slideShowTime = "Total Time: 0:00";
	
	/**
	 * Class constructor.
     * @param parent the instantiating Quickshow object 
     * @param control the ControlP5 object handling UI elements
	 */
	public controlbuttonUI(Quickshow parent, ControlP5 buttonUI){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
	    
		mainUIGroup = buttonUI.addGroup("buttonUI").setLabel("");
		
        lockButtons = new Button[12];
		
		//For the entire slideshow

		lockButtons[0] = playButton = buttonUI.addButton("Play")
	        .setPosition(30, 10)
	        .setSize(70, 15)
	        .setGroup(mainUIGroup);
		playButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);

		lockButtons[1] = resetShowButton = buttonUI.addButton("Reset")
	        .setPosition(110, 10)
	        .setSize(70, 15)
	        .setGroup(mainUIGroup);
        resetShowButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		shuffleToggle = buttonUI.addToggle("Shuffle Slides")
	        .setPosition(530, 10)
	        .setSize(15, 15)
	        .setCaptionLabel(" Shuffle Slides")
	        .setGroup(mainUIGroup);
		shuffleToggle.getCaptionLabel()
		    .align(ControlP5Constants.RIGHT_OUTSIDE, ControlP5Constants.CENTER);
		
		//For audioList
		lockButtons[2] = clearSongsButton = buttonUI
	        .addButton("Clear selected songs")
	        .setPosition(675, 402)
	        .setSize(200, 15)
	        .setGroup(mainUIGroup);
		clearSongsButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		//For the Thumbnail selector
		lockButtons[3] = selectAllImages = buttonUI
			.addButton("Select All Pictures")
		    .setPosition(140, 402)
		    .setSize(140,15)
		    .setGroup(mainUIGroup);
		selectAllImages.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockButtons[4] = selectAllVideos = buttonUI
			.addButton("Select All Clips")
		    .setPosition(290, 402)
		    .setSize(115, 15)
		    .setGroup(mainUIGroup);
		selectAllVideos.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockButtons[5] = clearVisualTimeline = buttonUI
	        .addButton("Clear slides")
		    .setPosition(30, 402)
		    .setSize(100,15)
		    .setGroup(mainUIGroup);
		clearVisualTimeline.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockButtons[6] = upButton = buttonUI.addButton("Up")
		    .setPosition(465, 402)
		    .setSize(50,15)
		    .setGroup(mainUIGroup);
		upButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockButtons[7] = downButton = buttonUI.addButton("Down")
		    .setPosition(600, 402)
		    .setSize(50,15)
		    .setGroup(mainUIGroup);
		downButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		//To control the visual timeline thumbnail
		lockButtons[8] = nextSlides = buttonUI.addButton("Next")
		    .setPosition(802, 580)
		    .setSize(69, 15)
		    .setGroup(mainUIGroup);
		nextSlides.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockButtons[9] = prevSlides = buttonUI.addButton("Previous")
		    .setPosition(648, 580)
		    .setSize(69, 15)
		    .setGroup(mainUIGroup);
		prevSlides.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		//Load media
		lockButtons[10] = loadMedia = buttonUI.addButton("Load Media")
		    .setPosition(675, 10)
		    .setSize(200, 15)
		    .setGroup(mainUIGroup);
		loadMedia.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockButtons[11] = editVisualItem = buttonUI
			.addButton("Visual Item Properties")
	        .setPosition(30, 580)
	        .setSize(175, 15)
	        .setGroup(mainUIGroup);
		editVisualItem.getCaptionLabel().alignX(ControlP5Constants.CENTER);

		pageIndex = buttonUI.addButton("pageIndex")
			.setPosition(520, 402)
			.setSize(75, 15)
			.setCaptionLabel(indexString)
			.lock()
			.setGroup(mainUIGroup);
		pageIndex.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		timeLineIndex = buttonUI.addButton("timeLineIndex")
			.setPosition(722, 580)
			.setSize(75, 15)
			.setCaptionLabel(timeLineIndexString)
			.lock()
			.setGroup(mainUIGroup);
		timeLineIndex.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		//For audio timeline control
		songTitle = buttonUI.addButton("songTitle")
			.setPosition(30, 490)
			.setSize(350, 15)
			.setCaptionLabel(songTitleString)
			.lock()
			.setGroup(mainUIGroup);
		timeLineIndex.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		totalTime = buttonUI.addButton("totalTime")
			.setPosition(400, 580)
			.setSize(130, 15)
			.setCaptionLabel(slideShowTime)
			.lock()
			.setGroup(mainUIGroup);
		totalTime.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		popupGroup = buttonUI.addGroup("popupUI")
	        .setLabel("")
	        ;//.setVisible(false);
		
		imgDisplaySlider = buttonUI.addSlider("Image Display Time")
	        .setNumberOfTickMarks(4)
	        .setPosition(popupOrigin[0], popupOrigin[1] + 45)
	        .setRange(2f, 5f)
	        .lock()
	        .setGroup(popupGroup);
		imgDisplaySlider.getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
		
		popupAccept = buttonUI.addButton("Accept")
	//        .lock()
	        .setSize(70, 15)
	        .setPosition(popupOrigin[0] + 115, popupOrigin[1] + 80)
	        .setGroup(popupGroup);
		
		popupCancel = buttonUI.addButton("Cancel")
            .lock()
            .setSize(70, 15)
            .setPosition(popupOrigin[0] + 190, popupOrigin[1] + 80)
            .setGroup(popupGroup);
		
		tagField = buttonUI.addTextfield("tagText")
	        .setCaptionLabel("Set Caption Text")
	        .setPosition(popupOrigin[0], popupOrigin[1])
	        .setGroup(popupGroup);

		
	}
	
	/**
	 * Toggles the visibility of the UI elements.
	 * @param visible whether or not the UI elements should be visible
	 */
	public void toggle(boolean visible) {
	    mainUIGroup.setVisible(visible);
        
        for(Button button : lockButtons) {
            button.setLock(!visible);
        }
        
        shuffleToggle.setLock(!visible);
	}
	
	/**
	 * Sets the page indexing string for the label.
	 * @param pages the toal number of pages
	 * @param index the current page number
	 */
	public void setPageIndex(int pages, int index){
		indexString = index + " of " + pages;
		pageIndex.setCaptionLabel(indexString);
	}
	
	/**
	 * Sets the page indexing string for the visual timeline label.
	 * @param pages the total number of pages
	 * @param index the current page number
	 */
	public void setTimeLinePageIndex(int pages, int index) {
	    int min = 0, sec = 0;
	    
	    if(pages > 0) {
	        min = (index-1)*30/60;
	        sec = (index-1)*30%60;
	    }
	    
	    StringBuilder build = new StringBuilder(
            String.format("%d:%02d", min, sec));
	    
	    if(pages > 0) {
    	    min = index*30/60;
            sec = index*30%60;
	    }
        
	    build.append(String.format(" - %d:%02d", min, sec));
        
        timeLineIndexString = build.toString();
        timeLineIndex.setCaptionLabel(timeLineIndexString);
	}
	
	/**
	 * Sets the title of the song currently displayed in the audio timeline.  
	 * @param a the AudioItem
	 */
	public void setSongTitle(AudioItem a){
		songTitleString = "Song: " + 
			(
				a == null ?
				"" :
				a.getTitle() + " - "+ a.getAuthor() + " - " +a.getTime()
			);
		songTitle.setCaptionLabel(songTitleString);
	}

	/**
	 * Sets the slideshow time display at the bottom of the screen
	 * @param min
	 * @param sec
	 */
	public void setSlideShowTime(int length){
		int min = length/60;
		int sec = length%60;
		totalTime.setCaptionLabel(String.format("Total Time: %d:%02d", min, sec));
	}

	/**
	 * 
	 * @param toggle
	 */
    public void togglePopup(boolean toggle) {
        for(Button button : lockButtons) {
            button.setLock(!toggle);
        }
        
        shuffleToggle.setLock(!toggle);
        
        popupGroup.setVisible(toggle);
        
        
    }
    
    public boolean isPopupEnabled() {
        return popupGroup.isVisible();
    }
    
    public int[] getPopupOrigin() {
        return popupOrigin;
    }
}
