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
    private DropdownList mediaTypeList;
    
    private int currentDisplayIndex = 0;
    
    int thumbWidth, thumbHeight;
    int firstThumbX = 111, firstThumbY = 120;

    private boolean debug = true;
    
    private final String[] imgExt = {
        "bmp", "jpg", "png", "gif" 
    };
    
    private final String[] videoExt = {
        "mov", "avi", "mpg", "mp4"
    };
    
    private final String[] audioExt = {
        "mp3", "wav", "aiff", "au", "snd"
    };
    
    /**
     * Class constructor. 
     * @param parent the Quickshow object creating this instance
     */
    FileBrowser(Quickshow parent, String curDir) {
        this.parent = parent;
        this.curDir = (new File(curDir)).getAbsolutePath();
        
        thumbHeight = 102;
        thumbWidth = 136;
        
        control = new ControlP5(parent);
        
        fileNames = new ArrayList<String>();
        thumbs = new ArrayList<PImage>();
        selectedIndex = new ArrayList<Integer>(20);

        control.setFont(control.getFont().getFont(), 15);
        
        Group group = control.addGroup("fileBrowser").setLabel("");
        
        pathField = control.addTextfield("")
            .setText(this.curDir)
            .setPosition(30, 30)
            .setSize(840, 30)
            .setLock(true)
            .setFocus(false)
            .setVisible(false)
            .setGroup(group);
            
        openButton = control.addButton("openButton")
            .setCaptionLabel("Open")
            .setPosition(750, 540)
            .setSize(120, 30)
            .setVisible(false)
            .setLock(true)
            .setGroup(group);
        
        scrollUpButton = control.addButton("scrollUpButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 145)
            .setCaptionLabel("^")
            .setGroup(group);
        
        scrollTopButton = control.addButton("scrollTopButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 70)
            .setCaptionLabel("^\n^")
            .setGroup(group);
        
        scrollDownButton = control.addButton("scrollDownButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 380)
            .setCaptionLabel("v")
            .setGroup(group);
            
        scrollBottomButton = control.addButton("scrollBottomButton")
            .setSize(30, 75)
            .setVisible(false)
            .setLock(true)
            .setPosition(840, 455)
            .setCaptionLabel("v\nv")
            .setGroup(group);
        
        mediaTypeList = control.addDropdownList("mediaTypeList")
            .setPosition(30, 570)
            .setSize(710, 30)
            .setVisible(false)
            .setBarHeight(30)
            .setGroup(group);
        
        mediaTypeList.addItem("Visual (.bmp, .jpg, .png, .gif, .mov, .avi, .mpg, .mp4)", 0);
        mediaTypeList.addItem("Audio (.mp3, .wav, .aiff, .au, snd)", 1);
        
        //TODO instantiate page number label 
        
        changeDir(curDir, false);
    }
    
    /**
     * Callback method for handling ControlP5 UI events.
     * @param e the ControlEvent to handle
     */
    public void controlEvent(ControlEvent e) {
        switch(e.getName()) {
        case "openButton":
            openButton();
            break;
            
        case "scrollUpButton":
            scrollUpButton();
            break;
            
        case "scrollTopButton":
            scrollTopButton();
            break;
            
        case "scrollDownButton":
            scrollDownButton();
            break;
            
        case "scrollBottomButton":
            scrollBottomButton();
            break;
            
        case "mediaTypeList":
            mediaTypeList(e);
        }
    }
    
    /**
     * TODO implement open button actions
     */
    private void openButton() {
        if(debug) {
            parent.println("open button pressed");
        }
    }
    
    /**
     * ControlP5 UI handler. Displays the previous page.
     */
    private void scrollUpButton() {
        if(currentDisplayIndex > 0) {
            currentDisplayIndex -= 20;
        }
        
        if(debug) {
            parent.println("scroll up button pressed");
        }
    }
    
    /**
     * ControlP5 UI handler. Displays the first page.
     */
    private void scrollTopButton() {
        currentDisplayIndex = 0;
        
        if(debug) {
            parent.println("scroll top button pressed");
        }
    }
    
    /**
     * ControlP5 UI handler. Displays the next page.
     */
    private void scrollDownButton() {
        if(currentDisplayIndex + 20 < thumbs.size()) {
            currentDisplayIndex += 20;
        }
        
        if(debug) {
            parent.println("scroll down button pressed");
        }
    }
    
    /**
     * ControlP5 UI handler. Displays the last page.
     */
    private void scrollBottomButton() {
        currentDisplayIndex = (thumbs.size()/20) * 20;
        
        if(debug) {
            parent.println("scroll bottom button pressed");
        }
    }
    
    /**
     * ControlP5 UI handler. Switches the media file type being scanned for.
     * @param e the initiating ControlEvent
     */
    private void mediaTypeList(ControlEvent e) {
        if(e.getValue() == 0) {
            changeDir(curDir, false);
        }
        
        else {
            changeDir(curDir, true);
        }
    }
    
    /**
     * Callback method for drawing the FileBrowser UI.
     */
    public void draw() {
        //draw thumbnail window
        parent.fill(0xffffff);
        parent.stroke(0);
        parent.rectMode(parent.CORNERS);
        parent.rect(30, 70, 840, 530);
        
        //thumbnail pic
        parent.imageMode(parent.CENTER);
        
        //filename
        parent.textAlign(parent.CENTER, parent.CENTER);
        parent.textSize(15);
        parent.fill(0);
        
        //selected highlight
        parent.noFill();
        parent.rectMode(parent.CENTER);
        parent.stroke(0xff5522ff);
        
        short i, j, imgIndex;
        for(imgIndex = 0, i = 0; i < 4; i++) {
            //draw thumbnail rows
            for(j = 0; j < 5 && j+5*i < thumbs.size(); j++, imgIndex++) {
//            for(j = 0; j < 5; j++, imgIndex++) {
                //draw thumbnail columns
                parent.image(
                //    thumbs.get(0),
                    thumbs.get(currentDisplayIndex+j+5*i),
                    109 + j*162, 120 + i*115,
                    thumbWidth, thumbHeight
                );
                
                parent.text(
              //      fileNames.get(0),
                    fileNames.get(currentDisplayIndex+j+5*i),
                    111 + j*162,
                    165 + i*115
                );
                
                if(selectedIndex.contains((int)imgIndex)) {
                    parent.rect(111 + j*162, 130 + i*115, 125, 100);
                }
            }
        }
        
        //draw selection box
        if(isSelecting) {
            parent.rectMode(parent.CORNERS);
            parent.stroke(0xff00FF5E);
            parent.rect(selectBox[0], selectBox[1], selectBox[2], selectBox[3]);
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

        File file = new File(curDir);
        
        ArrayList<File> files = new ArrayList(Arrays.asList(file.listFiles()));
        Iterator<File> fileIter = files.iterator();
        
        String fileName, filePath;
        String[] fileNameParts;
        short i;
        PImage thumb = parent.loadImage("data/img/folderThumbNail.png");
        
        pathField.setText(file.getAbsolutePath());

        //directories listed first
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
                        thumb = parent.loadImage(filePath);
                        thumb.resize(thumbWidth, thumbHeight);

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
                            thumb = (new Movie(parent, filePath)).get();
                            thumb.resize(thumbWidth, thumbHeight);

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
     * Loads multiple audio files.
     * @param minim the Minim object controlling the audio
     * @return an ArrayList containing the selected AudioItems
     */
    public ArrayList<AudioItem> loadAudioMulti(ddf.minim.Minim minim) {
        ArrayList<AudioItem> audios = new ArrayList<AudioItem>();
        
        for(Integer index : selectedIndex) {
            audios.add(
                new AudioItem(
                    minim,
                    curDir + '/' + fileNames.get(currentDisplayIndex + index)
                )
            );
        }
        
        audios.trimToSize();
        return audios;
    }
    
    /**
     * Loads multiple visual media files. 
     * @return an ArrayList containing the selected VisualItems
     */
    public ArrayList<VisualItem> loadVisualMulti() {
        ArrayList<VisualItem> visuals = new ArrayList<VisualItem>();
        
        String[] fileNameParts;
        String fileName;
        short i;
        for(Integer index : selectedIndex) {
            fileName = fileNames.get(currentDisplayIndex + index);
            fileNameParts = fileName.split("\\.");
            
            //file is image
            for(i = 0; i < imgExt.length; i++) {
                if(fileNameParts[fileNameParts.length-1]
                    .equalsIgnoreCase(imgExt[i]))
                {
                    visuals.add(new ImageItem(parent, curDir + '/' + fileName));

                    break;
                }
            }
            
            //file is video
            if(i == imgExt.length) {
                visuals.add(new MovieItem(parent, curDir + '/' + fileName));
            }
        }
        
        visuals.trimToSize();
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
     * Handler for mouse click. Selects a single file if applicable.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void mouseClicked(int mouseX, int mouseY) {
        if(mouseX >= 30 && mouseX <= 840  && mouseY >= 70 && mouseY <= 530) {
            selectedIndex.clear();
            
            short row = (short)((mouseY - 68)/115);
            short col = (short)((mouseX - 61)/162);
            
            boolean rowSelect = row*115 + 130 - 50 <= mouseY &&
                row*115 + 130 + 50 >= mouseY;
            boolean colSelect = col*162 + 111 - 62 <= mouseX &&
                col*162 + 111 + 62 >= mouseX;
            
            if(rowSelect && colSelect) {
                selectedIndex.add(currentDisplayIndex+5*row+col);
            }
                    
            if(debug) {
                parent.println(
                    "mouse clicked: " + mouseX + ',' + mouseY +
                    ", thumbnail: " + row + ' ' + col +
                    ", rowSelect: " + rowSelect + ", colSelect: " + colSelect +
                    ", selectedIndex: " + selectedIndex.get(0)
                );
            }
        }
    }
    
    /**
     * Constrains the mouse coordinates within the item window.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     * @return an integer array containing the adjusted coordinates
     */
    private int[] constrainMouse(int mouseX, int mouseY) {
        int[] result = new int[2];
        
        if(mouseX < 30) {
            result[0] = 30;
        }
        
        else if(mouseX > 840) {
            result[0] = 840;
        }
        
        else {
            result[0] = mouseX;
        }
        
        if(mouseY < 70) {
            result[1] = 70;
        }
        
        else if(mouseY > 530) {
            result[1] = 530;
        }
        
        else {
            result[1] = mouseY;
        }
        
        return result;
    }
    
    /**
     * Handler for mouse dragging. Updates the corners of the selection box if
     *   applicable.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void mouseDragged(int mouseX, int mouseY) {
        if(isSelecting) {
            int tmp[] = constrainMouse(mouseX, mouseY);
            
            selectBox[2] = tmp[0];
            selectBox[3] = tmp[1];
            
            if(debug) {
                parent.println("mouse dragged: " + tmp[0] + ' ' + tmp[1]);
            }
        }
    }
    
    /**
     * Handler for mouse release. Selects all items within the selection box if
     *   applicable.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void mouseReleased(int mouseX, int mouseY) {
        selectedIndex.clear();
        
        isSelecting = false;
        
        int minSelX = (selectBox[0] > selectBox[2] ? selectBox[2] : selectBox[0]);
        int maxSelX = (selectBox[0] < selectBox[2] ? selectBox[2] : selectBox[0]);
        int minSelY = (selectBox[1] > selectBox[3] ? selectBox[3] : selectBox[1]);
        int maxSelY = (selectBox[1] < selectBox[3] ? selectBox[3] : selectBox[1]);
        
        short maxRow = (short)((maxSelY - 68)/115);
        short minRow = (short)((minSelY - 68)/115);
        short maxCol = (short)((maxSelX - 61)/162);
        short minCol = (short)((minSelX - 61)/162);
        
        boolean rowSelect, colSelect;
        short i, j;
        for(i = minRow; i <= maxRow; i++) {
            rowSelect = true;
            
            if(i == minRow) {
                rowSelect = i*115 + 130 > minSelY;
            }
            
            else if(i == maxRow) {
                rowSelect = i*115 + 130 < maxSelY;
            }
            
            for(j = minCol; j <= maxCol; j++) {
                colSelect = true;
                
                if(j == minCol) {
                    colSelect = j*162 + 111 > minSelX;
                }
                
                else if(j == maxCol) {
                    colSelect = j*162 + 111 < maxSelX;
                }
                
                if(rowSelect && colSelect) {
                    selectedIndex.add(currentDisplayIndex+i*5+j);
                }
            }
        }
        
        if(debug) {
            parent.println("mouse released: " + mouseX + ' ' + mouseY + 
                "\nminSel: " + minRow + ' ' + minCol + 
                "\nmaxSel: " + maxRow + ' ' + maxCol);
        }
    }
    
    /**
     * Handler for mouse press. Initializes the multiple selection box if
     *   applicable.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void mousePressed(int mouseX, int mouseY) {
        if(mouseX >= 30 && mouseX <= 840  && mouseY >= 70 && mouseY <= 530) {
            selectBox[0] = selectBox[2] = mouseX;
            selectBox[1] = selectBox[3] = mouseY;
            
            isSelecting = true;
        }
        
        if(debug) {
            parent.println("mouse pressed: " + mouseX + ' ' + mouseY);
        }
    }
    
    /**
     * Toggles display of the FileBrowser.
     * @param visible whether the FileBrowser should be visible 
     * @param isAudioMode whether the FileBrowser is scanning for audio files
     */
    public void toggle(boolean visible, boolean isAudioMode) {
        pathField.setVisible(visible);
                
        openButton.setVisible(visible).setLock(!visible);
        
        scrollUpButton.setVisible(visible).setLock(!visible);
        
        scrollTopButton.setVisible(visible).setLock(!visible);
        
        scrollDownButton.setVisible(visible).setLock(!visible);
            
        scrollBottomButton.setVisible(visible).setLock(!visible);
        
        mediaTypeList.setVisible(visible);    
    }
    
    /**
     * Returns the status of the FileBrowser.
     * @return true if the FileBrowser is visible   
     */
    public boolean isEnabled() {
        return pathField.isVisible();
    }
    
}
