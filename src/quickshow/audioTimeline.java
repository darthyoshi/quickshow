/**
 * @file audioTimeline.java
 * @author Moses Lee
 * @description Renders the Quickshow audio timeline
 */

package quickshow;

import java.util.Arrays;
import java.util.ArrayList;

import processing.core.PConstants;
import quickshow.datatypes.AudioItem;
import ddf.minim.*;
import ddf.minim.analysis.FFT;


public class audioTimeline {
	private Quickshow parent;
	
	private boolean debug;
	
	//Generate the wave form image
	private ArrayList<float[][]> spectraList;
	private AudioSample audioClip;
	private static final int timeLineWidth = 840;
	private static final int timeLineHeight = 78;
	private ArrayList <AudioItem> selectedSongs;
	private int index;
	private int num_pages;
	static final int[] bounds = {30, 420, 870, 498};
	
	/**
	 * Class constructor.
	 * @param q the instantiating Quickshow object
     * @param minim the Minim object handling the audio files
	 */
	public audioTimeline(Quickshow q, Minim minim){
	    parent = q;
	    
	    debug = parent.getDebugFlag();

	    selectedSongs = new ArrayList<AudioItem>();
	    
	    spectraList = new ArrayList<float[][]>();
	}
	
	/**
	 * Callback method from drawing the timeline background.
	 */
	public void drawBackgroundCanvas(){
		parent.rectMode(PConstants.CORNER);
    	parent.imageMode(PConstants.CENTER);
    	
		parent.fill(90,90,90);
		parent.stroke(0);
		parent.rect(bounds[0], bounds[1], timeLineWidth, timeLineHeight);
/*
		parent.stroke(0xffffffff);
		short x;
		for(short i = 1; i < 30; i++) {
			x = (short)(i*28 + 30);

            parent.stroke((i%5 == 0 ? 0xff55aaff : 0xffffffff));
            parent.line(x, bounds[1]+2, x, bounds[3]-2);
		}*/
	}
	
	/*
	 * Referenced from processing forums. Modified function to suit 
	 * the needs of the application
	 */
	/**
	 * TODO add method header
	 */
	private void generateWaveForms() {
		int i,j,k;
		float leftChannel[];
		float leftSample[];
		int fftSize = 1024;
		int leftChunk;
		int leftchunkSize;
		int chunkStartIndex = 0;
		FFT fft;
		float leftSpectra[][];
		
		spectraList.clear();
		
		for(k = 0; k < selectedSongs.size(); k++) {
			//Will have to generate for all songs, but choose at index 0 for now
			audioClip = selectedSongs.get(k).getAudioSample();
			
			fft = new FFT( fftSize, audioClip.sampleRate());
	
			//Get audio sample from both channels
			//if(!(audioClip.getChannel(AudioSample.LEFT) == null))
			leftChannel = audioClip.getChannel(AudioSample.LEFT);
	
			//Allocate array for both channel sample
			leftSample = new float[fftSize];
	
			//Calculate chunks for both channel
			leftChunk = (leftChannel.length/fftSize + 1);
			//Allocate the spectra for both channels
			
			leftSpectra = new float[leftChunk][fftSize/2];
	
			//generate waveform data for both channels
			for(i = 0; i < leftChunk; ++i) {
				chunkStartIndex = i * fftSize;
				
				// the chunk size will always be fftSize, except for the 
				// last chunk, which will be however many samples are left in source
				leftchunkSize = Math.min(leftChannel.length - chunkStartIndex, fftSize);
				// copy first chunk into our analysis array
				System.arraycopy( leftChannel, // source of the copy
						chunkStartIndex, // index to start in the source
						leftSample, // destination of the copy
						0, // index to copy to
						leftchunkSize // how many samples to copy
						);
				
				// if the chunk was smaller than the fftSize, we need to pad the analysis buffer with zeroes        
				if ( leftchunkSize < fftSize ){
					// we use a system call for this
					Arrays.fill( leftSample, leftchunkSize, leftSample.length - 1, 0.0f );
				}
				
				// now analyze this buffer
				fft.forward(leftSample);
				// and copy the resulting spectrum into our spectra array
				for(j = 0; j < 512; ++j) {
					leftSpectra[i][j] = fft.getBand(j);
				}
			}
			
			spectraList.add(leftSpectra);
		}
	}
	
	/**
	 * Callback method for drawing the waveforms.
	 */
	public void drawWaveform(){
		if(!spectraList.isEmpty()) {
			parent.stroke(0);

			float[][] leftSpectra = spectraList.get(index); 
		
			float scaleMod = ((float) timeLineWidth / (float)leftSpectra.length);
			
			int i;
			float total;
			
			for(int s = 0; s < leftSpectra.length; s++) {
			    i = 0;
			    total = 0; 
			    for(i = 0; i < leftSpectra[s].length-1; i++){
			        total += leftSpectra[s][i];
			    }
			    total = total / 120;
			    parent.line((s*scaleMod) + 30,(bounds[1]+bounds[3])/2 + total,
			    			(s*scaleMod) + 30,(bounds[1]+bounds[3])/2 - total);
			}
		}
	}
	
	/**
	 * Retrieve the song list.
	 * @param songList the list of AudioItems
	 */
	public void receiveSelectedSongs(ArrayList <AudioItem> songList){
		selectedSongs = songList;
		num_pages = songList.size();
		index = 0;
		generateWaveForms();
	}
	
	/**
	 * Draws the timeline marker.
	 * @param mouseX the x-coordinates of the mouse
	 * @param mouseY the y-coordinates of the mouse
	 */
	public void displayTimeMarkers(int mouseX, int mouseY){
		if(!selectedSongs.isEmpty()) {
			
			AudioItem tmp = getCurrSong();
			int currSongLength = tmp.getLength();
			
			//See where in the song to show time stamp
			float scaleFactor = (float) mouseX/ (float) timeLineWidth;
			
			float timeMarker = scaleFactor * currSongLength;
			
			int min = (int) timeMarker/60;
			int sec = (int) timeMarker%60;
			
			parent.fill(0xffffffff);
			parent.textAlign(PConstants.LEFT);
			parent.text(String.format("%d:%02d", min, sec), mouseX + 5, mouseY - 10);
			parent.text(tmp.getAuthor() + " - " + tmp.getTitle(), mouseX + 5, mouseY + 10);

			parent.stroke(0xffff0000);
			parent.line(mouseX, bounds[1] + 2 , mouseX, bounds[3] - 2);
		}
	}

	/**
	 * Goes to the next song.
	 */
	public void nextSong(){
		if(!selectedSongs.isEmpty()) {
			index = ((index+1) % num_pages);
		}
	}

	/**
	 * Goes to the previous song.
	 */
	public void prevSong(){
		if(!selectedSongs.isEmpty()) {
			index = ((--index) < 0 ? selectedSongs.size()-1 : index);
		}
	}	

	/**
	 * Retrieves the current song index.
	 * @return integer
	 */
	public int getCurSongIndex(){
		return (selectedSongs.isEmpty() ? 0 : index+1);
	}

	/**
	 * Retrieves the number of pages.
	 * @return integer
	 */
	public int getNumPages(){
		return (selectedSongs.isEmpty() ? 0 : num_pages);
	}
	
	/**
	 * Retrieves the current song.
	 */
	public AudioItem getCurrSong(){
		return (selectedSongs.isEmpty() ? null : selectedSongs.get(index));
	}

	/**
	 * Removes all songs from the timeline.
	 */
	public void clear() {
		selectedSongs.clear(); 
		spectraList.clear();
	}
}
