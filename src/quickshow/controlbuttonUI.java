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
import controlP5.Controller;
import controlP5.Group;
import controlP5.Toggle;

public class controlbuttonUI {
	private Group mainUIGroup;
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
	private Controller[] lockControllers;
	private Button pageIndex;
	private Button timeLineIndex;
	private Button songTitle;
	private Button songIndex;
	private Button totalTime;

	private String songTitleString = "Song: ";
	private String indexString = "0 of 0";
	private String timeLineIndexString = "0:00 - 0:00";
	private String slideShowTime = "Total Time: 0:00";
	
	/**
	 * Class constructor.
     * @param control the ControlP5 object handling UI elements
	 */
	public controlbuttonUI(ControlP5 buttonUI){
	    mainUIGroup = buttonUI.addGroup("buttonUI").setLabel("");
		
        lockControllers = new Controller[13];
		
		//For the entire slideshow
		lockControllers[0] = playButton = buttonUI.addButton("Play")
	        .setPosition(30, 10)
	        .setSize(70, 15)
	        .setGroup(mainUIGroup);
		playButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);

		lockControllers[1] = resetShowButton = buttonUI.addButton("Reset")
	        .setPosition(110, 10)
	        .setSize(70, 15)
	        .setGroup(mainUIGroup);
        resetShowButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockControllers[12] = shuffleToggle = buttonUI.addToggle("Shuffle Slides")
	        .setPosition(530, 10)
	        .setSize(15, 15)
	        .setCaptionLabel(" Shuffle Slides")
	        .setGroup(mainUIGroup);
		shuffleToggle.getCaptionLabel()
		    .align(ControlP5Constants.RIGHT_OUTSIDE, ControlP5Constants.CENTER);
		
		//For audioList
		lockControllers[2] = clearSongsButton = buttonUI
	        .addButton("Clear selected songs")
	        .setPosition(675, 402)
	        .setSize(200, 15)
	        .setGroup(mainUIGroup);
		clearSongsButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		//For the Thumbnail selector
		lockControllers[3] = selectAllImages = buttonUI
			.addButton("Select All Pictures")
		    .setPosition(140, 402)
		    .setSize(140,15)
		    .setGroup(mainUIGroup);
		selectAllImages.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockControllers[4] = selectAllVideos = buttonUI
			.addButton("Select All Clips")
		    .setPosition(290, 402)
		    .setSize(115, 15)
		    .setGroup(mainUIGroup);
		selectAllVideos.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockControllers[5] = clearVisualTimeline = buttonUI
	        .addButton("Clear slides")
		    .setPosition(30, 402)
		    .setSize(100,15)
		    .setGroup(mainUIGroup);
		clearVisualTimeline.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockControllers[6] = upButton = buttonUI.addButton("Up")
		    .setPosition(465, 402)
		    .setSize(50,15)
		    .setGroup(mainUIGroup);
		upButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockControllers[7] = downButton = buttonUI.addButton("Down")
		    .setPosition(600, 402)
		    .setSize(50,15)
		    .setGroup(mainUIGroup);
		downButton.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		//To control the visual timeline thumbnail
		lockControllers[8] = nextSlides = buttonUI.addButton("Next")
		    .setPosition(802, 580)
		    .setSize(69, 15)
		    .setGroup(mainUIGroup);
		nextSlides.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockControllers[9] = prevSlides = buttonUI.addButton("Previous")
		    .setPosition(648, 580)
		    .setSize(69, 15)
		    .setGroup(mainUIGroup);
		prevSlides.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		//Load media
		lockControllers[10] = loadMedia = buttonUI.addButton("Load Media")
		    .setPosition(675, 10)
		    .setSize(200, 15)
		    .setGroup(mainUIGroup);
		loadMedia.getCaptionLabel().alignX(ControlP5Constants.CENTER);
		
		lockControllers[11] = editVisualItem = buttonUI
			.addButton("Visual Item Properties")
	        .setPosition(30, 580)
	        .setSize(175, 15)
	        .setVisible(false)
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
	}
	
	/**
	 * Toggles the visibility of the UI elements.
	 * @param visible whether or not the UI elements should be visible
	 */
	public void toggle(boolean visible) {
	    mainUIGroup.setVisible(visible);
        
        setLock(!visible);
    }
	
	/**
	 * Sets the page indexing string for the label.
	 * @param pages the total number of pages
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
	 * Sets the slideshow time display at the bottom of the screen.
	 * @param min number of minutes in the slide show
	 * @param sec number of seconds in the slide show
	 */
	public void setSlideShowTime(int length){
		int min = length/60;
		int sec = length%60;
		totalTime.setCaptionLabel(String.format("Total Time: %d:%02d", min, sec));
	}
	
	/**
	 * Sets the main UI element interactivity.
	 * @param lock whether to lock the main UI elements
	 */
	public void setLock(boolean lock) {
        for(Controller control : lockControllers) {
            control.setLock(lock);
        }
	}
	
	/**
	 * Sets the visibility of the VisualItem properties button.
	 * @param show whether to show the button
	 */
	public void showCaptionButton(boolean show) {
		editVisualItem.setVisible(show);
		editVisualItem.setLock(!show);
	}
}
