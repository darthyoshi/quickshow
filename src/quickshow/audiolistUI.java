package quickshow;

import controlP5.*;

public class audiolistUI {
	//ControlP5 audioList;
	ListBox list;
	int num_items;
	final int width = 200;
	final int height = 350;
	final String title = "Songs/Audio";
	protected String [] selectedSongList;
	
	//Constructors
	public audiolistUI(ControlP5 audioList){
		this.num_items = 0;
		list = audioList.addListBox(title);
		list.setSize(width, height);
	}
	
	//Class method for audio list
	
	//add item to the audio list
	public void addToList(){
		
	}
	
	//Future feature to remove songs from the list
	public void removeFromList(){
		
	}
	
	//To select all items in the list
	public void selectAllSongs(){
		
	}
	
	//Clear songs in the selectedSongList
	public void clearSelectedSongs(){
		
	}
	
	
}