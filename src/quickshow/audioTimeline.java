package quickshow;

import java.util.Arrays;
import java.util.Vector;
import quickshow.datatypes.AudioItem;
import ddf.minim.*;
import ddf.minim.analysis.FFT;


public class audioTimeline {
	
	//Generate the wave form image
	float leftSpectra[][];
	float rightSpectra[][];
	Minim audiotest;
	AudioSample audioClip;
	private final int timeLineWidth = 840;
	private final int timeLineHeight = 65;
	Vector <AudioItem> selectedSongs;
	
	
	public audioTimeline(Quickshow q){
		audiotest = new Minim(q);
	}
	
	public void drawBackgroundCanvas(Quickshow q){
		q.rect(30, 425, timeLineWidth, timeLineHeight);
	}
	
	/*
	 * Referenced from processing forums. Modified function to suit 
	 * the needs of the application
	 * 
	 */
	//public void generateWaveForm(AudioSample audioClip){
	public void generateWaveForm(){	
		//For debugging purposes
		audioClip = audiotest.loadSample("data/audio/guitar_reverse_phase_sloweddown.mp3", 2048);
		
		float leftChannel[];
		float leftSample[];
		int fftSize = 1024;
		FFT fft = new FFT( fftSize, audioClip.sampleRate() );
		int leftChunk;
		
		//Get audio sample from both channels
		//if(!(audioClip.getChannel(AudioSample.LEFT) == null))
			leftChannel = audioClip.getChannel(AudioSample.LEFT);
			
		//Allocate array for both channel sample
		leftSample = new float[fftSize];
		
		//Calculate chunks for both channel
		leftChunk = leftChannel.length/fftSize + 1;
		
		//Allocate the spectra for both channels
		leftSpectra = new float[leftChunk][fftSize/2];
		
		//generate waveform data for both channels
		  for(int i = 0; i < leftChunk; ++i) {
		    int chunkStartIndex = i * fftSize;
		   
		    // the chunk size will always be fftSize, except for the 
		    // last chunk, which will be however many samples are left in source
		    int leftchunkSize = min( leftChannel.length - chunkStartIndex, fftSize );
		    //int rightchunkSize = min( rightChannel.length - chunkStartIndex, fftSize );
		   
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
	
	public void drawWaveform(Quickshow q){
		//float scaleMod = 0.08f; //((float)width / (float)leftSpectra.length);
		float scaleMod = ((float) timeLineWidth / (float)leftSpectra.length);
		
		  for(int s = 0; s < leftSpectra.length; s++) {
		    //stroke(255);
		    int i = 0;
		    float total = 0; 
		    for(i = 0; i < leftSpectra[s].length-1; i++){
		        total += leftSpectra[s][i];
		    }
		    total = total / 40;
		    
		    q.line((s*scaleMod) + 30,total+460,(s*scaleMod) + 30,-total+460);
		  }
	}
	
	public void sendSelectedSongs(Vector <AudioItem> songList){
		selectedSongs = songList;
	}
	
	//private Helper Functions for generation of waveform data
	private int min(int i, int j){
		if (i < j) return i;
		else return j;
	}
}
