package quickshow;

import processing.core.PApplet;
import controlP5.*;
import ddf.*;


public class Quickshow extends PApplet {
	ControlP5 audioList;
	audiolistUI audioListbox;
	
	ControlP5 visualThumbnail;
	visualthumbnailUI thumbnails;
	
	public void setup() {
		setSize(900, 600);
		audioList = new ControlP5(this);
		audioListbox = new audiolistUI(audioList);
		
		visualThumbnail = new ControlP5(this);
		thumbnails = new visualthumbnailUI(visualThumbnail);
	}

	public void draw() {
	}
	
	public void update(){
	}
}