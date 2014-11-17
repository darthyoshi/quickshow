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

		//buttonUI.addListener(l);
		playButton = buttonUI.addButton("Play")
	        .setPosition(30, 10)
	        .setGroup(group);
		playButton.getCaptionLabel().alignX(buttonUI.CENTER);
		
		shareExportButton = buttonUI.addButton("Share/Export")
	        .setPosition(120, 10)
	        .setGroup(group);
		
		resetShowButton = buttonUI.addButton("Reset")
	        .setPosition(210, 10)
	        .setGroup(group);
		
		shuffleToggle = buttonUI.addToggle("Shuffle Slides")
	        .setMode(ControlP5.SWITCH)
	        .setPosition(300, 10)
	        .setCaptionLabel("Shuffle Slides")
	        .setGroup(group);
		
		//For audioList
		clearSongsButton = buttonUI.addButton("Clear selected songs")
	        .setPosition(675, 400)
	        .setSize(193, 15)
	        .setGroup(group);
		
		//For the Thumbnail selector
		selectAllImages = buttonUI.addButton("Select All Pictures")
		    .setPosition(200, 400)
		    .setSize(150,15)
		    .setGroup(group);
		
		selectAllVideos = buttonUI.addButton("Select All Clips")
		    .setPosition(370, 400)
		    .setSize(150, 15)
		    .setGroup(group);
		
		clearVisualTimeline = buttonUI.addButton("Clear slides")
		    .setPosition(30, 400)
		    .setSize(150,15)
		    .setGroup(group);
		
		//To control the visual timeline thumbnail
		nextSlides = buttonUI.addButton("Next")
		    .setPosition(805, 570)
		    .setGroup(group);
		
		prevSlides = buttonUI.addButton("Previous")
		    .setPosition(30, 570)
		    .setGroup(group);
		
		//Load media
		loadMedia = buttonUI.addButton("Load Media")
		    .setPosition(675, 10)
		    .setSize(193, 15)
		    .setGroup(group);
	}
	

}
