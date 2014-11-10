package quickshow;

import processing.core.PApplet;
import controlP5.*;
import ddf.*;


public class Quickshow extends PApplet {
	ControlP5 audioList;
	audiolistUI audioListbox;
	
	public void setup() {
		setSize(900, 600);
		audioList = new ControlP5(this);
		audioListbox = new audiolistUI(audioList);
	}

	public void draw() {
	}
	
	public void update(){
	}
}