package quickshow;

import quickshow.datatypes.AudioItem;
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
    
    private Group group;
    private Button playButton;
    private Button shareExportButton;
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
	private Button editVisualItem, removeNote;
	private Button[] lockButtons;
	private Button pageIndex;
	private Button timeLineIndex;
	private Button songTitle;
	private Button songIndex;
	private Button nextSong;
	private Button prevSong;
	private String songIndexString = "0 of 0";
	private String songTitleString = "Song: ";
	private String indexString = "0 of 0";
	private String timeLineIndexString = "0 of 0";
	
	/**
	 * Class constructor.
     * @param parent the instantiating Quickshow object 
     * @param control the ControlP5 object handling UI elements
	 */
	public controlbuttonUI(Quickshow parent, ControlP5 buttonUI){
	    this.parent = parent;
	    
	    debug = parent.getDebugFlag();
	    
		group = buttonUI.addGroup("buttonUI").setLabel("");
		
        lockButtons = new Button[14];
		
		//For the entire slideshow

		lockButtons[0] = playButton = buttonUI.addButton("Play")
	        .setPosition(30, 10)
	        .setSize(70, 15)
	        .setGroup(group);
		playButton.getCaptionLabel().alignX(buttonUI.CENTER);

		lockButtons[1] = resetShowButton = buttonUI.addButton("Reset")
	        .setPosition(110, 10)
	        .setSize(70, 15)
	        .setGroup(group);
        resetShowButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		shuffleToggle = buttonUI.addToggle("Shuffle Slides")
	        //.setMode(ControlP5.SWITCH)
	        .setPosition(530, 10)
	        .setSize(15, 15)
	        .setCaptionLabel(" Shuffle Slides")
	        .setGroup(group);
		shuffleToggle.getCaptionLabel()
		    .align(buttonUI.RIGHT_OUTSIDE, buttonUI.CENTER);
		
		//For audioList
		lockButtons[2] = clearSongsButton = buttonUI
	        .addButton("Clear selected songs")
	        .setPosition(675, 402)
	        .setSize(200, 15)
	        .setGroup(group);
		clearSongsButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		//For the Thumbnail selector
		lockButtons[3] = selectAllImages = buttonUI
			.addButton("Select All Pictures")
		    .setPosition(140, 402)
		    .setSize(140,15)
		    .setGroup(group);
		selectAllImages.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[4] = selectAllVideos = buttonUI
			.addButton("Select All Clips")
		    .setPosition(290, 402)
		    .setSize(115, 15)
		    .setGroup(group);
		selectAllVideos.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[5] = clearVisualTimeline = buttonUI
	        .addButton("Clear slides")
		    .setPosition(30, 402)
		    .setSize(100,15)
		    .setGroup(group);
		clearVisualTimeline.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[6] = upButton = buttonUI.addButton("Up")
		    .setPosition(465, 402)
		    .setSize(50,15)
		    .setGroup(group);
		upButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[7] = downButton = buttonUI.addButton("Down")
		    .setPosition(600, 402)
		    .setSize(50,15)
		    .setGroup(group);
		downButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		//To control the visual timeline thumbnail
		lockButtons[8] = nextSlides = buttonUI.addButton("Next")
		    .setPosition(802, 580)
		    .setSize(69, 15)
		    .setGroup(group);
		nextSlides.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[9] = prevSlides = buttonUI.addButton("Previous")
		    .setPosition(648, 580)
		    .setSize(69, 15)
		    .setGroup(group);
		prevSlides.getCaptionLabel().alignX(buttonUI.CENTER);
		
		//Load media
		lockButtons[10] = loadMedia = buttonUI.addButton("Load Media")
		    .setPosition(675, 10)
		    .setSize(200, 15)
		    .setGroup(group);
		loadMedia.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[11] = editVisualItem = buttonUI
			.addButton("Visual Item Properties")
	        .setPosition(30, 580)
	        .setSize(175, 15)
	        .setGroup(group);
		editVisualItem.getCaptionLabel().alignX(buttonUI.CENTER);

		pageIndex = buttonUI.addButton("pageIndex")
			.setPosition(520, 402)
			.setSize(75, 15)
			.setCaptionLabel(indexString)
			.lock()
			.setGroup(group);
		pageIndex.getCaptionLabel().alignX(buttonUI.CENTER);
		
		timeLineIndex = buttonUI.addButton("timeLineIndex")
			.setPosition(722, 580)
			.setSize(75, 15)
			.setCaptionLabel(timeLineIndexString)
			.lock()
			.setGroup(group);
		timeLineIndex.getCaptionLabel().alignX(buttonUI.CENTER);
		
		//For audio timeline control
		songTitle = buttonUI.addButton("songTitle")
			.setPosition(30, 490)
			.setSize(350, 15)
			.setCaptionLabel(songTitleString)
			.lock()
			.setGroup(group);
		timeLineIndex.getCaptionLabel().alignX(buttonUI.CENTER);
		
		songIndex = buttonUI.addButton("songIndex")
			.setPosition(720, 490)
			.setSize(75, 15)
			.setCaptionLabel(songIndexString)
			.lock()
			.setGroup(group);
		songIndex.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[12] = nextSong = buttonUI.addButton("nextSong")
			.setPosition(800, 490)
			.setSize(70, 15)
			.setCaptionLabel("Next Song")
			.setGroup(group);
		nextSong.getCaptionLabel().alignX(buttonUI.CENTER);
		
		lockButtons[13] = prevSong = buttonUI.addButton("prevSong")
			.setPosition(645, 490)
			.setSize(70, 15)
			.setCaptionLabel("Prev Song")
			.setGroup(group);
		prevSong.getCaptionLabel().alignX(buttonUI.CENTER);
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
	 * Sets the page indexing string for the label.
	 * @param pages the toal number of pages
	 * @param index the current page number
	 */
	public void setPageIndex(int pages, int index){
		indexString = index + " of " + pages;
		pageIndex.setCaptionLabel(indexString);
	}
	
	/**
	 * Sets the page indexing string for the label for the visual timeline.
	 * @param pages the total number of pages
	 * @param index the current page number
	 */
	public void setTimeLinePageIndex(int pages, int index){
		timeLineIndexString = index + " of " + pages;
		timeLineIndex.setCaptionLabel(timeLineIndexString);
	}
	
	/**
	 * Sets the title of the song currently displayed in the audio timeline.  
	 * @param a the AudioItem
	 */
	public void setSongTitle(AudioItem a){
		if(a == null) songTitleString = "Song: ";
		else songTitleString = "Song: " + a.getTitle() + " - "+ a.getAuthor() + " - "+a.getTime();
		songTitle.setCaptionLabel(songTitleString);
	}
	
	/**
	 * Sets the page indexing string for the label for the audio timeline.
	 * @param pages the total number of pages 
	 * @param index the curent page number
	 */
	public void setSongTimelinePageIndex(int pages, int index){
		songIndexString = index + " of " + pages;
		songIndex.setCaptionLabel(songIndexString);
	}
}
