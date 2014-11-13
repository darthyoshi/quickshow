package quickshow;

import ddf.minim.*;

class AudioItem {
    private AudioPlayer audio;
    private String title;
    private String author;
    private int length;
    private AudioSample sample;
    
    public AudioItem(Minim minim, String filename) {
        audio = minim.loadFile(filename);
        
        //Need this to generate waveform in the UI
        sample = minim.loadSample(filename);
        
        AudioMetaData meta = audio.getMetaData();
        
        title = meta.title();
        author = meta.author();
        length = meta.length()/1000;
    }

    public AudioPlayer getAudio() {
        return audio;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getLength() {
        return length;
    }
    
    public AudioSample getMinimObject(){
    	return sample;
    }
}
