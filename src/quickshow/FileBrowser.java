package quickshow;

import java.util.*;
import java.io.*;
import processing.core.*;
import processing.video.*;

public class FileBrowser {
    private Quickshow parent;
    private String imgDir, audioDir;
    
    private ArrayList<AudioItem> audios;
    private ArrayList<Object> visuals;
    private HashMap<String, PImage> thumbs;
    
    int thumbWidth, thumbHeight;
    
    final String[] imgExt = {
        "bmp", "jpg", "png", "gif" 
    };
    
    final String[] videoExt = {
        "mov", "avi", "mpg", "mp4"
    };
    
    final String[] audioExt = {
        "mp3", "wav", "aiff", "au", "snd"
    };
    
    /**
     * Class constructor. 
     * @param parent the Quickshow object creating this instance
     * @param thumbWidth the thumbnail width
     * @param thumbHeight the thumbnail height
     */
    FileBrowser(Quickshow parent, int thumbWidth, int thumbHeight) {
        this.parent = parent;
        this.thumbHeight = thumbHeight;
        this.thumbWidth = thumbWidth;
        
        audios = new ArrayList<AudioItem>();
        visuals = new ArrayList<Object>();
        thumbs = new HashMap<String, PImage>();
    }
    
    /**
     * TODO implement
     */
    public void updateAndDraw() {
        //draw stuff
    }
    
    /**
     * Changes the FileBrowser directory.
     * @param newDir the new directory path
     * @param isAudioMode specifies whether the FileBrowser instance is reading
     *   audio files
     */
    private void changeDir(String newDir, boolean isAudioMode) {
        thumbs.clear();
        
        String[] fileNames = (new File(newDir)).list();
        String[] fileNameParts;
        int i;
        PImage thumb, src;
        
        if(isAudioMode) {
            audioDir = newDir;
            
            thumb = parent.loadImage("data/img/audioThumbNail.png");
            
            for(String fileName : fileNames) {
                fileNameParts = fileName.split("\\.");
                
                for(i = 0; i < audioExt.length; i++) {
                    if(fileNameParts[fileNameParts.length-1].equalsIgnoreCase(audioExt[i])) {
                        thumbs.put(fileName, thumb);
                    }
                }
            }
        }

        else {
            imgDir = newDir;
            
            thumb = parent.createImage(thumbWidth, thumbHeight, parent.RGB);
        
            for(String fileName : fileNames) {
                fileNameParts = fileName.split("\\.");
                
                //create image thumbnail
                for(i = 0; i < imgExt.length; i++) {
                    if(fileNameParts[fileNameParts.length-1].equalsIgnoreCase(imgExt[i])) {
                        src = parent.loadImage(fileName);
                        
                        thumb.copy(src, 0, 0, src.width, src.height, 0, 0, thumbWidth, thumbHeight);

                        thumbs.put(fileName, thumb);

                        break;
                    }
                }
                
                //if item wasn't image
                if(i == imgExt.length) {
                    //create thumbnail of 1st frame of video
                    for(i = 0; i < videoExt.length; i++) {
                        if(fileNameParts[fileNameParts.length-1].equalsIgnoreCase(videoExt[i])) {
                            src = (new Movie(parent, fileName)).get();

                            thumb.copy(src, 0, 0, src.width, src.height, 0, 0, thumbWidth, thumbHeight);

                            thumbs.put(fileName, thumb);

                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * TODO implement loading audio file
     * @return
     */
    public AudioItem loadAudio() {
        AudioItem result = null;
        
        return result;
    }
    
    /**
     * TODO implement loading image file
     * @return
     */
    public PImage loadImg() {
        PImage result = null;
        
        return result;
    }
    
    /**
     * TODO implement loading video file
     * @return
     */
    public Movie loadVideo() {
        Movie result = null;
        
        return result;
    }
    
    /**
     * TODO implement loading multiple audio files
     * @return
     */
    public ArrayList<AudioItem> loadAudioMulti() {
        return audios;
    }
    
    /**
     * TODO implement loading multiple image and/or video files 
     * @return
     */
    public ArrayList<Object> loadVisualMulti() {
        return visuals;
    }
}
