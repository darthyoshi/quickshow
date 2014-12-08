/**
 * @file audiolistUI.java
 * @author Moses Lee
 * @description A class for displaying and selecting audio items.
 */

package quickshow;

import java.util.ArrayList;
import java.util.ListIterator;

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

    /**
     * Class constructor.
     * @param parent the instantiating Quickshow object
     * @param audioList the ControlP5 object handling UI elements
     */
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


    /**
     * Remove a song from the list.
     * @param itemToRemove the song label
     */
    public void removeFromList(String itemToRemove){
        //Remove song
        list.removeItem(itemToRemove);

        //Add a place holder
        ListBoxItem placeHolder = list.addItem("empty", num_items+1);
        placeHolder.setColorBackground(0xffff0000);
    }

    /**
     * Clear songs in the selectedSongList.
     */
    public void clearSelectedSongs(){
        selectedSongList.clear();
    }

    /**
     * Add the song to selected song list.
     * @param index the index of the selected song
     */
    public void addToSelectedSongs(int index){
        AudioItem selectedSong;
        if(MAX_SONGS > selectedSongList.size() && num_items > 0 && index < num_items){

            selectedSong = songList.get(index);

            selectedSongList.add(selectedSong);
        }
    }

    /**
     * Check if a song is selected.
     * @param songName the song label
     * @return true if the selected songs list contains a song matching songName
     */
    public boolean isSongSelected(String songName){
        return selectedSongList.contains(songName);
    }

    /**
     * Retrieves the selected songs.
     * @return an ArrayList containing the selected AudioItems
     */
    public ArrayList<AudioItem> returnSelectedSongList(){
        return selectedSongList;
    }

    /**
     * Adds the loaded songs to the available song list.
     * @param fileList an ArrayList containing the AudioItems to be added
     */
    public void receiveSongs(ArrayList <AudioItem> fileList){
        songList.addAll(fileList);

        if(debug) {
            Quickshow.println("Size of fileList: " + fileList.size());
        }
        //Display the songs on the list
        ListIterator<AudioItem> songIter = songList.listIterator(oldListSize);
        while(songIter.hasNext()){
            addToList(songIter.next());
        }
        oldListSize = songList.size();
    }


    /**
     * Toggles display of the audiolistUI.
     * @param visible whether the audiolistUI should be visible
     */
    public void toggle(boolean visible){
        group.setVisible(visible);
    }

    /*
     * Helper functions
     */
    //TODO Make sure to add more songs in the future and update the list
    /**
     * Adds an item to the audio list.
     * @param audio the AudioItem to add
     */
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