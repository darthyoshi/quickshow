package quickshow.datatypes;

import ddf.minim.*;

public class AudioItem {
    private AudioPlayer audio;
    private String title;
    private String author;
    private int length;
    private AudioSample sample;
    
    /**
     * Class constructor.
     * @param minim the Minim object controlling the audio
     * @param filename the file name of the audio file to load
     */
    public AudioItem(Minim minim, String filename) {
        audio = minim.loadFile(filename);
        
        //Need this to generate waveform in the UI
        sample = minim.loadSample(filename, 2048);
        
        AudioMetaData meta = audio.getMetaData();
        
        title = meta.title();
        author = meta.author();
        length = meta.length()/1000;
    }

    /**
     * Retrieves the AudioPlayer object associated with the audio file.
     * @return an AudioPlayer object
     */
    public AudioPlayer getAudio() {
        return audio;
    }
    
    /**
     * Retrieves the author of the audio file.
     * @return the author as a String
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Retrieves the title of the audio file.
     * @return the title as a String
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Retrieves the length of the audio file.
     * @return the length in seconds
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Retrieves the AudioSample object associated with the audio file.
     * @return an AudioSample object
     */
    public AudioSample getAudioSample(){
    	return sample;
    }
}
