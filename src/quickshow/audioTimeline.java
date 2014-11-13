package quickshow;

import java.util.Arrays;
import java.lang.System.*;
import ddf.minim.*;
import ddf.minim.analysis.FFT;

public class audioTimeline {
	
	//Generate the wave form image
	float leftSpectra[][];
	float rightSpectra[][];
	Minim audiotest;
	AudioSample audioClip;
	
	public audioTimeline(Quickshow q){
		audiotest = new Minim(q);
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
		float rightChannel[];
		float leftSample[];
		float rightSample[];
		int fftSize = 1024;
		FFT fft = new FFT( fftSize, audioClip.sampleRate() );
		int leftChunk;
		int rightChunk;
		
		//Get audio sample from both channels
		//if(!(audioClip.getChannel(AudioSample.LEFT) == null))
			leftChannel = audioClip.getChannel(AudioSample.LEFT);
			
		//if(!(audioClip.getChannel(AudioSample.RIGHT) == null))
			//rightChannel = audioClip.getChannel(AudioSample.RIGHT);
		
		//Allocate array for both channel sample
		leftSample = new float[fftSize];
		//rightSample = new float[fftSize];
		
		//Calculate chunks for both channel
		leftChunk = leftChannel.length/fftSize + 1;
		//rightChunk = rightChannel.length/fftSize + 1;
		
		//Allocate the spectra for both channels
		leftSpectra = new float[leftChunk][fftSize/2];
		//rightSpectra = new float[rightChunk][fftSize/2];
		
		//generate waveform data for both channels
		  for(int i = 0; i < leftChunk; ++i)
		  {
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
		    
		   /* arraycopy( rightChannel, // source of the copy
		               chunkStartIndex, // index to start in the source
		               leftSample, // destination of the copy
		               0, // index to copy to
		               rightchunkSize // how many samples to copy
		              );*/
		      
		    // if the chunk was smaller than the fftSize, we need to pad the analysis buffer with zeroes        
		    if ( leftchunkSize < fftSize )
		    {
		      // we use a system call for this
		      Arrays.fill( leftSample, leftchunkSize, leftSample.length - 1, 0.0f );
		    }
		    /*if ( rightchunkSize < fftSize )
		    {
		      // we use a system call for this
		      Arrays.fill( rightSample, rightchunkSize, rightSample.length - 1, 0.0f );
		    }*/
		    
		    // now analyze this buffer
		    fft.forward(leftSample);
		    //fft.forward(rightSample);
		   
		    // and copy the resulting spectrum into our spectra array
		    for(int j = 0; j < 512; ++j)
		    {
		      leftSpectra[i][j] = fft.getBand(j);
		      //rightSpectra[i][j] = fft.getBand(j);
		    }
		  }
		
	}
	
	public void drawWaveform(Quickshow q){
		int width = 300;
		int height = 300;
		float scaleMod = ((float)width / (float)leftSpectra.length);
		
		  for(int s = 0; s < leftSpectra.length; s++)
		  {
		    //stroke(255);
		    int i = 0;
		    float total = 0; 
		    for(i = 0; i < leftSpectra[s].length-1; i++)
		    {
		        total += leftSpectra[s][i];
		    }
		    total = total / 10;
		    
		    q.line(s*scaleMod,total+height/2,s*scaleMod,-total+height/2);
		    //System.out.println("leftSpectra is: "+ leftSpectra[s][i]);
		  }
		  
		  q.line(leftSpectra.length * scaleMod, 1000, leftSpectra.length *scaleMod, 0+300);
	}
	
	//Helper Functions for generation of waveform data
	private int min(int i, int j){
		if (i < j) return i;
		else return j;
	}
//	private void arraycopy(float[] channel, int chunkStartIndex, float[] destSample, int i, int chunkSize){
//		for (int j = chunkStartIndex; j < chunkSize; j++, i++){
//			destSample[i] = channel[j];
//		}
//	}
	
}
