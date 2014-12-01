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
		    total = total / 80;
		    parent.line((s*scaleMod) + 30,total+460,(s*scaleMod) + 30,-total+460);
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
	 * 
	 * @param visible
	 */
	public void toggle(boolean visible){
		
	}
	
	/**
	 * 
	 */
	public void nextSong(){
		index = ((index+1) % num_items) + 0;
		System.out.println("Index is: " + index);
	}
	
	/**
	 * 
	 */
	public void prevSong(){
		index--;
		if (index < 0) index = selectedSongs.size()-1;
	}
	
	
	/**
	 * 
	 */
	public int getIndex(){
		return index+1;
	}
	
	/**
	 * 
	 */
	public int getNumSelectedSongs(){
		return num_items;
	}
	
	/**
	 * 
	 * 
	 */
	public AudioItem getCurrSong(){
		return selectedSongs.get(index);
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
