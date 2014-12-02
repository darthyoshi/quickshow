package quickshow;

import java.util.Arrays;
import java.util.ArrayList;
import quickshow.datatypes.AudioItem;
import ddf.minim.*;
import ddf.minim.analysis.FFT;


public class audioTimeline {
	private Quickshow parent;
	
	private boolean debug;
	
	//Generate the wave form image
	private float leftSpectra[][];
	private AudioSample audioClip;
	private static final int timeLineWidth = 840;
	private static final int timeLineHeight = 65;
	private ArrayList <AudioItem> selectedSongs;
	private int index;
	private int num_items;
	
	/**
	 * Class constructor.
	 * @param q the instantiating Quickshow object
     * @param minim the Minim object handling the audio files
	 */
	public audioTimeline(Quickshow q, Minim minim){
	    parent = q;
	    
	    debug = parent.getDebugFlag();

	    selectedSongs = new ArrayList<AudioItem>();
	}
	
	/**
	 * TODO add method header
	 */
	public void drawBackgroundCanvas(){
		parent.rectMode(parent.CORNER);
		parent.rect(30, 420, timeLineWidth, timeLineHeight);
		
	}
	
	/*
	 * Referenced from processing forums. Modified function to suit 
	 * the needs of the application
	 * 
	 */
    public void generateWaveForm(){	
		if(selectedSongs.size() == 0) return;
		
		float leftChannel[];
		float leftSample[];
		int fftSize = 1024;
		int leftChunk = 0;
		int chunkStartIndex = 0;
		//Will have to generate for all songs, but choose at index 0 for now
		audioClip = selectedSongs.get(index).getAudioSample();
		
		FFT fft = new FFT( fftSize, audioClip.sampleRate());

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
		for(int i = 0; i < leftChunk; ++i) {
			chunkStartIndex = i * fftSize;
			
			// the chunk size will always be fftSize, except for the 
			// last chunk, which will be however many samples are left in source
			int leftchunkSize = min( leftChannel.length - chunkStartIndex, fftSize );
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
			for(int j = 0; j < 512; ++j) {
				leftSpectra[i][j] = fft.getBand(j);
			}
		}
	}
	
	/**
	 * Draw the waveforms.
	 */
	public void drawWaveform(){
		if(selectedSongs.size() == 0) return;
		
		float scaleMod = ((float) timeLineWidth / (float)leftSpectra.length);
		
		for(int s = 0; s < leftSpectra.length; s++) {
		    int i = 0;
		    float total = 0; 
		    for(i = 0; i < leftSpectra[s].length-1; i++){
		        total += leftSpectra[s][i];
		    }
		    total = total / 120;
		    parent.line((s*scaleMod) + 30,total+450,(s*scaleMod) + 30,-total+450);
		}
	}
	
	/**
	 * Retrieve the song list.
	 * @param songList the list of AudioItems
	 */
	public void receiveSelectedSongs(ArrayList <AudioItem> songList){
		selectedSongs = songList;
		num_items = songList.size();
		index = 0;
	}
	
	/**
	 * For the audiotimeline marker
	 * @return Returns a string thats in the MM:SS format
	 */
	public void displayTimeMarkers(int x, int y){
		if(selectedSongs.size() == 0) return;
		
		//To offset from the left hand side
		x -= 30;
		
		int currSongLength = getCurrSong().getLength();
		
		//See where in the song to show time stamp
		float scaleFactor = (float) x/ (float) timeLineWidth;
		
		float timeMarker = scaleFactor * currSongLength;
		
		int min = (int) timeMarker/60;
		int sec = (int) timeMarker%60;
		
		//Do we want a box or just the text?
		parent.fill(0xffffffff);
		parent.text(String.format("%d:%02d", min, sec), x, y);
	}
	
	
	/**
	 * 
	 * @param visible
	 */
	public void toggle(boolean visible){
		
	}
	
	/**
	 * 
	 */
	public void nextSong(){
		if(selectedSongs.size() == 0) return;
		index = ((index+1) % num_items);
	}
	
	/**
	 * 
	 */
	public void prevSong(){
		if(selectedSongs.size() == 0) return;
		index--;
		if (index < 0) index = selectedSongs.size()-1;
	}
	
	
	/**
	 * 
	 */
	public int getIndex(){
		if(selectedSongs.size() == 0) return 0;
		else return index+1;
	}
	
	/**
	 * 
	 */
	public int getNumSelectedSongs(){
		if(selectedSongs.size() == 0) return 0;
		else return num_items;
	}
	
	/**
	 * 
	 * 
	 */
	public AudioItem getCurrSong(){
		if(selectedSongs.size() == 0) return null;
		else return selectedSongs.get(index);
	}
	
	/**
	 * @param i
	 * @param j
	 * @return
	 */
	
	//private Helper Functions for generation of waveform data.
	private int min(int i, int j){
		return (i < j ? i : j);
	}
}
