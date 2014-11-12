package quickshow;

import controlP5.*;
import java.util.Vector;

public class audiolistUI {
	//ControlP5 audioList;
	ListBox list;
	int num_items;
	final int width = 200;
	final int height = 350;
	final String title = "Songs/Audio";
	//protected String [] selectedSongList;
	Vector <String> selectedSongList; 
	Vector <AudioItem> songList;
	
	//Constructors
	public audiolistUI(ControlP5 audioList){
		this.num_items = 0;
		audioList.setFont(audioList.getFont().getFont(), 15);
		list = audioList.addListBox(title);
		list.setSize(width, height);
		list.setPosition(675, 50);
		list.disableCollapse();
		list.setBarHeight(18);
		
		//Vectors to store information about the Listbox
		selectedSongList = new Vector<String>();
		songList = new Vector<AudioItem>();
		
		//Need to find a way to display the list without initializing
		for (int i=0;i<30;i++) {
			ListBoxItem lbi = list.addItem("empty", i);
			lbi.setColorBackground(0xffff0000);
		}
	}
	
	//Class method for audio list
	
	//add item to the audio list
	public void addToList(AudioItem audio){
		num_items++;
		
		//Removes place holder item
		list.removeItem("empty");
		
		//Generate the Label for the listBoxItem
		String songTitle = audio.getAuthor() + " - " + audio.getTitle() + " - "+ audio.getLength();
		
		//Adds the actual song
		ListBoxItem songToAdd = list.addItem(songTitle, num_items+1);
		songToAdd.setColorBackground(0xffff0000);
	}
	
	//Future feature to remove songs from the list
	public void removeFromList(String itemToRemove){
		//Remove song
		list.removeItem(itemToRemove);
		
		//decrement counter
		num_items--;
		
		//Add a place holder
		ListBoxItem placeHolder = list.addItem("empty", num_items+1);
		placeHolder.setColorBackground(0xffff0000);
	}
	
	//To select all items in the list
	public void selectAllSongs(){
		for (int i = 0; i < songList.size(); i++){
			String itemToAdd = songList.get(i).getAuthor() + " - " + songList.get(i).getTitle() 
								+ " - "+ songList.get(i).getLength();
			
			if(!selectedSongList.contains(itemToAdd)) selectedSongList.add(itemToAdd);
		}
	}
	
	//Clear songs in the selectedSongList
	public void clearSelectedSongs(){
		selectedSongList.clear();
	}
	
	//Add the song to selected song list
	public void addToSelectedSongs(String nameOfSelectedSongs){
		selectedSongList.add(nameOfSelectedSongs);
	}
	
	//Check if song is selected
	public boolean isSongSelected(String songName){
		return selectedSongList.contains(songName);
	}

}