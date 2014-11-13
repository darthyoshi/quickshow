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
	
	//Test variables for debug purposes
	audioTimeline aT;
	
	public void setup() {
		setSize(900, 600);
		audioList = new ControlP5(this);
		audioListbox = new audiolistUI(audioList);
		rect(30, 30, 620, 400);
		visualThumbnail = new ControlP5(this);
		aT = new audioTimeline(this);
		aT.generateWaveForm();
	}

	public void draw() {
		
		//Background for the thumbnails
		rect(30, 30, 620, 375);
		fill(255,0,0);
		
		
		aT.drawWaveform(this);
		
	}
	
	public void update(){
	}
}