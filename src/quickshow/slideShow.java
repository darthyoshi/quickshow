/**
 * @file slideShow.java
 * @author Kay Choi, Moses Lee
 * @description TODO add description 
 */

package quickshow;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PConstants;
import processing.core.PImage;
import processing.video.Movie;
import quickshow.datatypes.AudioItem;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.MovieItem;
import quickshow.datatypes.VisualItem;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Toggle;

public class slideShow {
    private Quickshow parent;
    
    private boolean debug;
   
    private Group group;
    private Button stopButton;
    private Toggle playToggle;

    private PImage curFrame, transitFrame;
    private int[] transDelta = {0, 0}, transDir = {1, 1};
    private boolean transit = false;
    private Movie movie;
    
    private int frameWidth, frameHeight;
    
    private ArrayList<AudioItem> audios;
    private ArrayList<VisualItem> visuals;
    private Iterator<VisualItem> visualIter = null;
    private Iterator<AudioItem> audioIter = null;
    private AudioItem curAudioItem = null;
    private VisualItem curVisualItem = null;
    
    private ArrayList<String> curTagTexts;
    private ArrayList<Float[]> curTagTimes;
    private String tagText = null;
    private Float[] tagTime = null;
    
    private float curImgTime;
    
    private boolean isPlaying = false, isEnabled = false, shuffle = false;

    /**
     * Class constructor.
     * @param parent the instantiating Quickshow object 
     * @param control the ControlP5 object handling UI elements
     */
    public slideShow(Quickshow parent, ControlP5 control) {
        this.parent = parent;
        
        debug = parent.getDebugFlag();
  
        audios = new ArrayList<AudioItem>();
        visuals = new ArrayList<VisualItem>();
        
        curTagTexts = new ArrayList<String>();
        curTagTimes = new ArrayList<Float[]>();
        
        group = control.addGroup("slideShow")
            .setCaptionLabel("")
            .setVisible(false);
        
        playToggle = control.addToggle("playToggle")
            .setCaptionLabel("")
            .setPosition(10, 10)
            .setSize(30, 30)
            .setGroup(group)
            .setImages(
                parent.loadImage("data/img/playbutton.png"),
                null,
                parent.loadImage("data/img/pausebutton.png"),
                null
            ).setLock(true);

        stopButton = control.addButton("stopButton")
            .setCaptionLabel("")
            .setLock(true)
            .setPosition(50, 10)
            .setSize(30, 30)
            .setImage(parent.loadImage("data/img/stopbutton.png"))
            .setGroup(group);
        
        curFrame = parent.createImage(0, 0, PConstants.RGB);
    }
    
    /**
     * Populates the audio component of the slide show.
     * @param newAudio an ArrayList containing the new AudioItems
     */
    public void addAudio(ArrayList<AudioItem> newAudio) {
        audios.addAll(newAudio);
        
        if(debug) {
            Quickshow.println("#audio items in slide show: " + audios.size());
        }
        
        if(!shuffle) {
            audioIter = audios.iterator();
        }
        
        nextAudioItem();
    }
    
    /**
     * Populates the visual component of the slide show.
     * @param newVisual an ArrayList containing the new VisualItems
     */
    public void addVisual(ArrayList<VisualItem> newVisual) {
        visuals.addAll(newVisual);
        
        if(debug) {
            Quickshow.println("#visual items slide show: " + visuals.size());
        }
        
        if(!shuffle) {
            visualIter = visuals.iterator();
        }

        nextVisualItem();
    }
    
    /**
     * Callback method for handling ControlP5 UI events.
     * @param e the ControlEvent to handle
     */
    public void controlEvent(ControlEvent e) {
        switch(e.getName()) {
        case "playToggle":
            playToggle(playToggle.getState());
            
            break;
            
        case "stopButton":
            stopButton();
            
            break;
        }
    }
    
    /**
     * Callback method for displaying the slide show.
     */
    public void draw() {
        parent.background(0xff555555);
        
        if(isPlaying) {
            if(!transit) {
                if(curAudioItem != null) {
                    if(!curAudioItem.getAudio().isPlaying()) {
                        nextAudioItem();
                    }
                }
                
                if(curVisualItem != null) {
                    curImgTime += 1f/parent.frameRate;
                    
                    if(movie != null) {
	                    if(movie.available()) {
	                        movie.read();
	                    }
	                    
	                    curFrame = movie.get();
                    }
                        
                    if(curImgTime >= (float)curVisualItem.getDisplayTime()) {
                        if(debug) {
                            Quickshow.println("slide show transition begin");
                        }
                        
                        curImgTime = 0f;
                        
                        transit = true;
                        
                        //create transition frame
                        transitFrame = parent.createImage(
                            parent.width,
                            parent.height,
                            PConstants.RGB
                        );
                        transitFrame.loadPixels();
                        for(
                            int i = 0;
                            i < transitFrame.pixels.length;
                            i++
                        ) {
                            transitFrame.pixels[i] = 0xff555555;
                        }
                        transitFrame.updatePixels();
                        transitFrame.set(
                            (parent.width - curFrame.width)/2,
                            (parent.height - curFrame.height)/2,
                            curFrame
                        );
                        
                        //set horizontal transition direction
                        double rand = Math.random();
                        transDir[0] = (rand < 0.33 ? 1 : 
                            (rand < 0.66 ? 0 : -1));
                        
                        //set vertical transition direction
                        rand = Math.random();
                        if(transDir[0] != 0) {
                            transDir[1] = (rand < 0.33 ? 1 : 
                                (rand < 0.66 ? 0 : -1));
                        }
                        
                        else {
                            transDir[1] = (rand < 0.5 ? 1 : -1);
                        }
                        
                        nextVisualItem();
                        
                        if(movie != null && movie.available()) {
                            movie.read();
                            
                            curFrame = movie.get();
                        }
                    }                        

                    if(frameWidth != curFrame.width ||
                        frameHeight != curFrame.height)
                    {
                        calcFrameDims();
        
                        curFrame.resize(frameWidth, frameHeight);
                    }
                }
                
                else {
                    stopButton();
                }
            }
        }
        
        parent.imageMode(PConstants.CENTER);
        parent.image(curFrame, parent.width/2, parent.height/2);
        
        if(!transit) {
            if(tagTime != null) {
                if(
                    (movie != null && movie.time() > tagTime[0] && 
                    movie.time() < tagTime[1]) ||
                    (curImgTime > tagTime[0] && curImgTime < tagTime[1])
                ) {
                    parent.fill(0xffffffff);
                    parent.textAlign(PConstants.CENTER, PConstants.CENTER);
                    parent.text(tagText, parent.width/2, parent.height * 11/12);
                }
                    
                else if((movie != null && movie.time() > tagTime[1]) ||
            		curImgTime > tagTime[1])
                {
                    nextTag();
                }
            }
        }
        
        else {
            parent.image(
                transitFrame,
                parent.width/2 + transDelta[0],
                parent.height/2 + transDelta[1]
            );
            
            if(isPlaying) {
                transDelta[0] += (int)(1f/25f*parent.width) * transDir[0];
                transDelta[1] += (int)(1f/25f*parent.height) * transDir[1];
                
                if(
                    (int)(1.5f*parent.width) <= transDelta[0] ||
                    (int)(-1.5f*parent.width) >= transDelta[0] ||
                    (int)(1.5f*parent.height) <= transDelta[1] ||
                    (int)(-1.5f*parent.height) >= transDelta[1]
                ) {
                    transDelta[0] = transDelta[1] = 0;
                    
                    transit = false;
                    
                    if(debug) {
                        Quickshow.println("slide show transition end");
                    }
                }
            }
        }
    }
    
    /**
     * ControlP5 UI handler. Pauses and resumes slide show playback.
     * @param mode the new playback mode
     */
    public void playToggle(boolean mode){
        isPlaying = mode;
        
        playToggle.setValue(isPlaying);
        
        if(debug) {
            Quickshow.println("slide show playing: " + isPlaying);
        }

        if(!isPlaying) {
            if(curAudioItem != null) {
                curAudioItem.getAudio().pause();
            }
            
            if(movie != null) {
                movie.pause();
            }
        }
        
        else {
            if(curAudioItem != null) {
                curAudioItem.getAudio().play();
            }

            if(movie != null) {
                movie.play();
            }
        }
    }
    
    /**
     * Calculates the dimensions of the VisualItem frame.
     */
    private void calcFrameDims() {
        float aspect = 1f * curFrame.width / curFrame.height;
    
        frameWidth = (curFrame.width > parent.width ?
            parent.width : curFrame.width);
        frameHeight = (int)(frameWidth / aspect);
        
        if(frameHeight > parent.height) {
            frameHeight = parent.height;
            frameWidth = (int)(frameHeight * aspect);
        }
    }
    
    /**
     * Prepares the next AudioItem in the playlist.
     */
    private void nextAudioItem() {
        if(shuffle && !audios.isEmpty()) {
            int shuffleIndex = (int)(Math.random()*audios.size());
            
            curAudioItem = audios.get(shuffleIndex);
            
            audios.remove(shuffleIndex);
        }
        
        else if(audioIter != null && audioIter.hasNext()) {
            curAudioItem = audioIter.next();
            curAudioItem.getAudio().play();
        }
        
        else {
            curAudioItem = null;
        }
    }
    
    /**
     * Retrieves the next annotation
     */
    private void nextTag() {
        if(!curTagTimes.isEmpty()) {
            int i = 0;
            float min = Float.MAX_VALUE;
            Float[] times;
            
            for(int j = 0; j < curTagTimes.size(); j++) {
                if((times = curTagTimes.get(j))[0] < min) {
                    min = times[0];
                    i = j;
                }
            }
            
            tagTime = curTagTimes.remove(i);
            tagText = curTagTexts.remove(i);
            
            if(debug) {
                Quickshow.println("next tag: \"" + tagText + "\", from " +
                    tagTime[0] + "s to " + tagTime[1] + 's');
            }
        }
        
        else {
            tagText = null;
            tagTime = null;
            
            if(debug) {
                Quickshow.println("No tags to show");
            }
        }
    }
    
    /**
     * Prepares the next VisualItem in the playlist.
     */
    private void nextVisualItem() {
        movie = null;
        
        if(shuffle && !visuals.isEmpty()) {
            int shuffleIndex = (int)(Math.random()*visuals.size());
        
            curVisualItem = visuals.get(shuffleIndex);
            
            visuals.remove(shuffleIndex);
        }
        
        else if(visualIter != null && visualIter.hasNext()) {
            curVisualItem = visualIter.next();
        }
        
        else {
            stopButton();
        }
        
        if(curVisualItem != null) {
            curTagTexts.addAll(curVisualItem.getTagTexts());
            curTagTimes.addAll(curVisualItem.getTagTimes());
            
            nextTag();
            
            if(curVisualItem.checkType().equals("video")) {
                movie = ((MovieItem)curVisualItem).getMovie();
                movie.play();
            }
            
            else {
                curFrame = ((ImageItem)curVisualItem).getImage();
            }
            
            if(debug) {
            	Quickshow.println(
        			"visual item type: " + curVisualItem.checkType() +
        			"\nduration: " + curVisualItem.getDisplayTime()
    			);
            }
        }
    }
    
    /**
     * ControlP5 UI handler. Stops slide show playback.
     */
    public void stopButton() {
        transit = isEnabled = false;
        
        playToggle.setState(isEnabled);
        
        audioIter = null;
        if(curAudioItem != null) {
            curAudioItem.getAudio().pause();
        }
        curAudioItem = null;

        visualIter = null;
        curVisualItem = null;
        
        if(movie != null) {
            movie.stop();
            movie = null;
        }
        
        toggleUI(false);
        
        visuals.clear();
        audios.clear();
        
        parent.toggleMain(true);
    }
    
    /**
     * Retrieves the current play mode of the slide show.
     * @return true if the slide show is playing
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Sets the visibility of the slide show UI components.
     * @param visible the visibility state
     */
    private void toggleUI(boolean visible) {
        group.setVisible(visible);
        
        playToggle.setLock(!visible);
        
        stopButton.setLock(!visible);
    }
    
    /**
     * Retrieves the current state of the slide show.
     * @return true if the slide show is active
     */
    public boolean isEnabled() {
        return isEnabled;
    }
    
    /**
     * Enables the slide show and begins playback.
     */
    public void startPlaying() {
        isEnabled = true;
        
        playToggle.setState(isEnabled);
        
        if(movie != null) {
            movie.play();
        }
        
        if(curAudioItem != null) {
            if(debug) {
                Quickshow.println("starting audio file");
            }
            curAudioItem.getAudio().play();
        }
        
        toggleUI(true);
        
        curImgTime = 0;
    }
    
    /**
     * Toggles the slide show shuffle.
     * @param shuffle whether or not to shuffle the slide show
     */
    public void toggleShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }
}
