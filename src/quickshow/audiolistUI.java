/**
 * @file audiolistUI.java
 * @author Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;

import quickshow.datatypes.AudioItem;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.ListBox;
import controlP5.ListBoxItem;

public class audiolistUI {
    private Quickshow parent;
    
    private boolean debug;
    
	private Group group;
	
	private ListBox list;
	private int num_items = 0;
	private final static int width = 200;
	private final static int height = 350;
	private final static int MAX_SONGS = 3;
	private final static String title = "Songs/Audio";
	private ArrayList <AudioItem> selectedSongList;
	private ArrayList <AudioItem> songList;

	private int oldListSize;
	
	//Constructors
	public audiolistUI(Quickshow parent, ControlP5 audioList){
		this.parent = parent;
		
		debug = parent.getDebugFlag();
		
		group = audioList.addGroup("AudioList").setLabel("");
		audioList.setFont(audioList.getFont().getFont(), 15);
		list = audioList.addListBox(title)
	        .setSize(width, height)
	        .setPosition(675, 50)
	        .disableCollapse()
	        .setBarHeight(18);
		list.setGroup(group);
		
		//Vectors to store information about the Listbox
		selectedSongList = new ArrayList<AudioItem>();
		songList = new ArrayList<AudioItem>();
		
		//Need to find a way to display the list without initializing
		ListBoxItem lbi;
		for (int i=0;i<25;i++) {
			lbi = list.addItem("empty", i);
			lbi.setColorBackground(0xffff0000);
			lbi.setId(i);
			lbi.setText("Empty");
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
	public void addToSelectedSongs(int index){
		AudioItem selectedSong;
		if(MAX_SONGS > selectedSongList.size() && num_items > 0 && index < num_items){
			
			selectedSong = songList.get(index);
			
			selectedSongList.add(selectedSong);
		}
	}
	
	//Check if song is selected
	public boolean isSongSelected(String songName){
		return selectedSongList.contains(songName);
	}
	
	//Return the vector with the selected songs
	public ArrayList<AudioItem> returnSelectedSongList(){
		return selectedSongList;
	}
	
	//Receive the list of songs
	public void receiveSongs(ArrayList <AudioItem> fileList){
		songList.addAll(fileList);
		
		if(debug) {
			Quickshow.println("Size of fileList: " + fileList.size());
		}
		//Display the songs on the list
		for(int i = oldListSize; i < songList.size(); i++){
			addToList(songList.get(i));
		}
		oldListSize = songList.size();
	}
	

	/**
	 * TODO add method header
	 * @param visible
	 */
	public void toggle(boolean visible){
		group.setVisible(visible);
	}

	/*
	 * Helper functions
	 */
	//TODO Make sure to add more songs in the future and update the list
	//add item to the audio list
	protected void addToList(AudioItem audio){
		
		String songDisplay = audio.getAuthor() + " - " + audio.getTitle() + " - "+ audio.getLength();
		
		if(debug) {
		    Quickshow.println("Song being added " + songDisplay);
		}
		
		ListBoxItem songToAdd;
		
		if(num_items > 25) {
			list.addItem(songDisplay, num_items);
		}
		else {
			songToAdd = list.getItem(num_items);
			songToAdd.setText(songDisplay);
		}
		//Generate the Label for the listBoxItem
		//Adds the actual song
		num_items++;
	}

}