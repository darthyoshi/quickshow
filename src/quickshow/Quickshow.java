package quickshow;

import processing.core.PApplet;
import quickshow.datatypes.AudioItem;
import controlP5.*;
import ddf.*;

import java.util.*;

public class Quickshow extends PApplet {
	ControlP5 audioList;
	audiolistUI audioListbox;
	
	ControlP5 visualThumbnail;
	visualthumbnailUI thumbnails;
	ArrayList <AudioItem> audioFiles;
	
	public void setup() {
		setSize(900, 600);
		audioList = new ControlP5(this);
		audioListbox = new audiolistUI(audioList);
		rect(30, 30, 620, 400);
		visualThumbnail = new ControlP5(this);
	}

	public void draw() {
		
		//Background for the thumbnails
		rect(30, 30, 620, 375);
		fill(255,0,0);
		
	}
	
	public void update(){
	}
}