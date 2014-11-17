package quickshow;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Toggle;

public class controlbuttonUI {
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
	
	ControlEvent events;
	ControlListener l;
	
	/*
	 * TODO make sure to get interactivity
	 */
	public controlbuttonUI(ControlP5 buttonUI){
		Group group = buttonUI.addGroup("buttonUI").setLabel("");
		
		//For the entire slideshow
		playButton = buttonUI.addButton("Play");
		playButton.setPosition(30, 10);
		playButton.getCaptionLabel().alignX(buttonUI.CENTER);
		playButton.setGroup(group);
		
		shareExportButton = buttonUI.addButton("Share/Export");
		shareExportButton.setPosition(120, 10);
		shareExportButton.setGroup(group);
		
		resetShowButton = buttonUI.addButton("Reset");
		resetShowButton.setPosition(210, 10);
		resetShowButton.setGroup(group);
		
		shuffleToggle = buttonUI.addToggle("Shuffle Slides");
		shuffleToggle.setMode(ControlP5.SWITCH);
		shuffleToggle.setPosition(300, 10);
		shuffleToggle.setCaptionLabel("Shuffle Slides");
		shuffleToggle.setGroup(group);
		
		//For audioList
		clearSongsButton = buttonUI.addButton("Clear selected songs");
		clearSongsButton.setPosition(675, 400);
		clearSongsButton.setSize(193, 15);
		clearSongsButton.setGroup(group);
		
		//For the Thumbnail selector
		selectAllImages = buttonUI.addButton("Select All Pictures");
		selectAllImages.setPosition(200, 400);
		selectAllImages.setSize(150,15);
		selectAllImages.setGroup(group);
		
		selectAllVideos = buttonUI.addButton("Select All Clips");
		selectAllVideos.setPosition(370, 400);
		selectAllVideos.setSize(150, 15);
		selectAllVideos.setGroup(group);
		
		clearVisualTimeline = buttonUI.addButton("Clear slides");
		clearVisualTimeline.setPosition(30, 400);
		clearVisualTimeline.setSize(150,15);
		clearVisualTimeline.setGroup(group);
		
		//To control the visual timeline thumbnail
		nextSlides = buttonUI.addButton("Next");
		nextSlides.setPosition(805, 570);
		nextSlides.setGroup(group);
		
		prevSlides = buttonUI.addButton("Previous");
		prevSlides.setPosition(30, 570);
		prevSlides.setGroup(group);
		
		//Load media
		loadMedia = buttonUI.addButton("Load Media");
		loadMedia.setPosition(675, 10);
		loadMedia.setSize(193, 15);
		loadMedia.setGroup(group);
		
		
	}
	
	
	public void controlEvent(ControlEvent theEvent) {
		  System.out.println(theEvent.getController().getName());
		 // n = 0;
	}
}
