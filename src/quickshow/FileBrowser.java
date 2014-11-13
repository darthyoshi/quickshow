/**
 * @file FileBrowser.java
 * @author Kay Choi
 * @description The Quickshow file browser class. 
 */
package quickshow;

import java.util.*;
import java.io.*;

import processing.core.*;
import processing.video.*;
import quickshow.datatypes.*;
import controlP5.*;

public class FileBrowser {
    private Quickshow parent;
    private String curDir;
    
    private ArrayList<AudioItem> audios;
    private ArrayList<VisualItem> visuals;
    private ArrayList<String> fileNames;
    private ArrayList<PImage> thumbs;
    private ArrayList<Integer> selectedIndex;
    
    private int[] selectBox = {0, 0, 0, 0};
    private boolean isSelecting = false;
    
    private ControlP5 control;
    private Button openButton;
    private Button scrollUpButton, scrollDownButton;
    private Button scrollBottomButton, scrollTopButton;
    private Textfield pathField;
    private Textlabel pageLabel;
    
    private int currentDisplayIndex = 0;
    
    int thumbWidth, thumbHeight;
    int firstThumbX = 111, firstThumbY = 120;
    
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
     */
    FileBrowser(Quickshow parent) {
        this.parent = parent;
        
        thumbHeight = 102;
        thumbWidth = 136;
        
        control = new ControlP5(parent);
        
        audios = new ArrayList<AudioItem>();
        visuals = new ArrayList<VisualItem>();
        fileNames = new ArrayList<String>();
        thumbs = new ArrayList<PImage>();
        selectedIndex = new ArrayList<Integer>();
        
        pathField = control.addTextfield("pathField", 30, 30, 840, 30)
            .setLock(true)
            .setFocus(false);
        
        openButton = control.addButton("openButton")
            .setCaptionLabel("Open")
            .setPosition(750, 540)
            .setSize(120, 30)
            .setVisible(false)
            .setLock(true);
        
        scrollUpButton = control.addButton("scrollUpButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 145)
            .setCaptionLabel("^");
        
        scrollTopButton = control.addButton("scrollTopButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 70)
            .setCaptionLabel("^\n^");
        
        scrollDownButton = control.addButton("scrollDownButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 380)
            .setCaptionLabel("^");
            
        scrollBottomButton = control.addButton("scrollBottomButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 455)
            .setCaptionLabel("^\n^");
        
        //TODO instantiate page number label 
        
    }
    
    /**
     * TODO implement open button actions
     */
    public void openButton() {
        
    }
    
    /**
     * TODO implement page up actions
     */
    public void scrollUpButton() {
        
    }
    
    /**
     * TODO implement page top actions
     */
    public void scrollTopButton() {
        
    }
    
    /**
     * TODO implement page down actions
     */
    public void scrollDownButton() {
        
    }
    
    /**
     * TODO implement page bottom actions
     */
    public void scrollBottomButton() {
        
    }
    
    /**
     * TODO implement FileBrowser GUI
     */
    void updateAndDraw() {
        //draw thumbnail window
        parent.rectMode(parent.CORNERS);
        parent.fill(0xffffff);
        parent.stroke(0);
        parent.rect(30, 70, 870, 530);
        
        parent.imageMode(parent.CENTER);
        int i, j;
        for(i = 0; i < 4; j++) {        //draw thumbnail rows
            for(j = 0; j < 5; j++) {    //draw thumbanil columns
                parent.image(
                    thumbs.get(currentDisplayIndex+j+5*i),
                    0/*center x of top left thumbnail*/, 0/*center y of top left thumbnail*/,
                    thumbWidth, thumbHeight);
            }
        }
        
        //draw selection box
        if(isSelecting) {
            parent.stroke(0x5522aa);
            parent.noFill();
            parent.rect(selectBox[0], selectBox[1], selectBox[2], selectBox[3]);
        }
        
        for(Integer select : selectedIndex) {
            //highlight selected thumbnails
        }
    }
    
    /**
     * Changes the FileBrowser directory.
     * @param newDir the new directory path
     * @param isAudioMode specifies whether the FileBrowser is currently reading
     *   audio files
     */
    private void changeDir(String newDir, boolean isAudioMode) {
        thumbs.clear();
        fileNames.clear();
        
        curDir = newDir;
        
        List<File> files = Arrays.asList((new File(newDir)).listFiles());
        Iterator<File> fileIter = files.iterator();
        
        String fileName, filePath;
        String[] fileNameParts;
        int i;
        PImage src, thumb = parent.loadImage("data/img/folderThumbNail.png");
        
        //directories listed first
        File file;
        while(fileIter.hasNext()) {
            file = fileIter.next();
            
            if(file.isDirectory()) {
                fileNames.add(file.getName());
                thumbs.add(thumb);
                
                fileIter.remove();
            }
        }
        
        //list audio files
        if(isAudioMode) {
            thumb = parent.loadImage("data/img/audioThumbNail.png");
            
            fileIter = files.iterator();
            while(fileIter.hasNext()) {
                fileName = fileIter.next().getName();
                fileNameParts = fileName.split("\\.");
                
                for(i = 0; i < audioExt.length; i++) {
                    if(fileNameParts[fileNameParts.length-1]
                        .equalsIgnoreCase(audioExt[i]))
                    {
                        fileNames.add(fileName);
                        thumbs.add(thumb);
                    }
                }
            }
        }

        //list image/video files
        else {
            thumb = parent.createImage(thumbWidth, thumbHeight, parent.RGB);
        
            fileIter = files.iterator();
            while(fileIter.hasNext()) { 
                fileName = fileIter.next().getName();
                fileNameParts = fileName.split("\\.");
                filePath = curDir + '/' + fileName;
                
                //create image thumbnail
                for(i = 0; i < imgExt.length; i++) {
                    if(fileNameParts[fileNameParts.length-1]
                        .equalsIgnoreCase(imgExt[i]))
                    {
                        src = parent.loadImage(filePath);
                        
                        thumb.copy(
                            src,
                            0, 0, src.width, src.height,
                            0, 0, thumbWidth, thumbHeight
                        );

                        fileNames.add(fileName);
                        thumbs.add(thumb);

                        break;
                    }
                }
                
                //if item wasn't image, it is video
                if(i == imgExt.length) {
                    //create thumbnail of 1st frame of video
                    for(i = 0; i < videoExt.length; i++) {
                        if(fileNameParts[fileNameParts.length-1]
                            .equalsIgnoreCase(videoExt[i]))
                        {
                            src = (new Movie(parent, filePath))
                                .get();

                            thumb.copy(
                                src,
                                0, 0, src.width, src.height,
                                0, 0, thumbWidth, thumbHeight
                            );

                            fileNames.add(fileName);
                            thumbs.add(thumb);

                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Loads an audio file.
     * @param minim the Minim object handling the audio file
     * @param filename the name of the audio file to load
     * @return an AudioItem object representing the loaded audio file 
     */
    public AudioItem loadAudio(ddf.minim.Minim minim, String filename) {
        return new AudioItem(minim, curDir + '/' + filename);
    }

    /**
     * Loads an image file.
     * @param filename the name of the image file to load
     * @return a PImage object representing the loaded image file 
     */
    public ImageItem loadImg(String filename) {
        return new ImageItem(parent, curDir + '/' + filename);
    }

    /**
     * Loads a video file.
     * @param filename the name of the video file to load
     * @return a Movie object representing the loaded video file 
     */
    public MovieItem loadVideo(String filename) {
        return new MovieItem(parent, curDir + '/' + filename);
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
    public ArrayList<VisualItem> loadVisualMulti() {
        return visuals;
    }
    
    /**
     * Returns the current FileBrowser directory.
     * @return the current FileBrowser directory path
     */
    public String getCurDir() {
        return curDir;
    }
    
    /**
     * TODO handle selecting single file
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    void mouseClicked(int mouseX, int mouseY) {
        
    }
    
    /**
     * Updates the corners of the multiple selection box.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    void mouseDragged(int mouseX, int mouseY) {
        int newX = 0, newY = 0;
        
        //TODO ensure rect bounds within thumbnail window
        //thumbWindowMinX < newX < thumbWindowMaxX
        //thumbWindowMinY < newY < thumbWindowMaxY
        
        selectBox[2] = newX;
        selectBox[3] = newY;
        
    }
    
    /**
     * TODO implement selecting multiple files
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    void mouseReleased(int mouseX, int mouseY) {
        mouseDragged(mouseX, mouseY);
        
        isSelecting = false;
    }
    
    /**
     * Initializes the multiple selection box.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    void mousePressed(int mouseX, int mouseY) {
        mouseDragged(mouseX, mouseY);
        
        selectBox[0] = selectBox[2];
        selectBox[1] = selectBox[3];
        
        isSelecting = true;
    }
}
