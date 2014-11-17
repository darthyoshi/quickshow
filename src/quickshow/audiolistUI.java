/**
 * @file audiolistUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import controlP5.*;

import java.util.ArrayList;

import quickshow.datatypes.AudioItem;

public class audiolistUI {
	//ControlP5 audioList;
	ListBox list;
	int num_items;
	private final int width = 200;
	private final int height = 350;
	private final int MAX_SONGS = 3;
	private final String title = "Songs/Audio";
	//protected String [] selectedSongList;
	ArrayList <AudioItem> selectedSongList;
	ArrayList <AudioItem> songList;
	
	//Constructors
	public audiolistUI(ControlP5 audioList){
		
		Group group = audioList.addGroup("AudioList").setLabel("");
		audioList.setFont(audioList.getFont().getFont(), 15);
		list = audioList.addListBox(title);
		list.setSize(width, height);
		list.setPosition(675, 50);
		list.disableCollapse();
		list.setBarHeight(18);
		list.setGroup(group);
		
		//Vectors to store information about the Listbox
		selectedSongList = new ArrayList<AudioItem>();
		songList = new ArrayList<AudioItem>();
		
		//Need to find a way to display the list without initializing
		for (int i=0;i<30;i++) {
			ListBoxItem lbi = list.addItem("empty", i);
			lbi.setColorBackground(0xffff0000);
		}
	}
	
	//Class method for audio list

	
	//Future feature to remove songs from the list
	public void removeFromList(String itemToRemove){
		//Remove song
		list.removeItem(itemToRemove);

		//Add a place holder
		ListBoxItem placeHolder = list.addItem("empty", num_items+1);
		placeHolder.setColorBackground(0xffff0000);
	}
	
	//Clear songs in the selectedSongList
	public void clearSelectedSongs(){
		selectedSongList.clear();
	}
	
	//Add the song to selected song list
	public void addToSelectedSongs(AudioItem selectedSong){
		if(MAX_SONGS > selectedSongList.size()) selectedSongList.add(selectedSong);
	}
	
	//Check if song is selected
	public boolean isSongSelected(String songName){
		return selectedSongList.contains(songName);
	}
	
	//Return the vector with the selected songs
	public ArrayList<AudioItem> sendSongList(){
		return selectedSongList;
	}
	
	//Receive the list of songs
	public void receiveSongs(ArrayList <AudioItem> fileList){
		songList = fileList;
		
		//Display the songs on the list
		for(int i = 0; i < songList.size(); i++){
			addToList(songList.get(i));
		}
	}
	
	/*
	 * Helper functions
	 */
	//add item to the audio list
	protected void addToList(AudioItem audio){
		
		//Removes place holder item
		list.removeItem("empty");
		
		//Generate the Label for the listBoxItem
		String songDisplay = audio.getAuthor() + " - " + audio.getTitle() + " - "+ audio.getLength();
		
		//Adds the actual song
		ListBoxItem songToAdd = list.addItem(audio.getTitle(), num_items+1);
		songToAdd.setText(songDisplay);
		songToAdd.setColorBackground(0xffff0000);
		songList.add(audio);
	}

}