/**
 * @file FileBrowser.java
 * @author Kay Choi
 * @description The Quickshow file browser class. 
 */

package quickshow;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PImage;
import processing.video.Movie;
import quickshow.datatypes.AudioItem;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.MediaItem;
import quickshow.datatypes.MovieItem;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.Group;
import controlP5.Textfield;

@SuppressWarnings("static-access")
public class FileBrowser {
    private boolean debug;
    
    private Quickshow parent;
    private String curDir;
    
    private ArrayList<String> fileNames;
    private ArrayList<PImage> thumbs;
    private ArrayList<Integer> selectedIndex;
    
    private ArrayList<MediaItem> results;
    
    private int[] selectBox = {0, 0, 0, 0};
    private boolean isSelecting = false;
    
    private long clickTime;
    private boolean dblClick = false;
    
    private Group group;
    private Button[] lockButtons;
    private Textfield pathField;
    private Button pageLabel;
    private DropdownList mediaTypeList;
    
    private ddf.minim.Minim minim;
    
    private int curDisplayIndex = 0;
    private boolean isAudioMode = false;
    
    int thumbWidth, thumbHeight;
    int firstThumbX = 111, firstThumbY = 120;

    private static final String[] imgExt = {
        "bmp", "jpg", "png", "gif" 
    };
    
    private static final String[] videoExt = {
        "mov", "avi", "mpg", "mp4"
    };
    
    private static final String[] audioExt = {
        "mp3", "wav", "aiff", "au", "snd"
    };
    
    /**
     * Class constructor.
     * @param parent the Quickshow object creating this instance 
     * @param minim the Minim object handling the audio file
     * @param control the ControlP5 object handling UI elements
     * @param curDir the initial FileBrowser directory
     */
	FileBrowser(Quickshow parent, ddf.minim.Minim minim, ControlP5 control, String curDir) {
        this.parent = parent;
        
        debug = parent.getDebugFlag();
        
        String[] pathParts = (new File(curDir)).getAbsolutePath().split("\\/");
        StringBuilder path = new StringBuilder();
        
        if(debug) {
            parent.print("path: ");
            for(String part:pathParts) {
                parent.print(part + '/');
            }
            parent.println();
        }
        
        for(short i = 0; i < pathParts.length - 1; i++) {
            path.append("/" + pathParts[i]);
        }
        
        this.curDir = path.toString();
        
        this.minim = minim;
        
        thumbHeight = 102;
        thumbWidth = 136;
        
        fileNames = new ArrayList<String>();
        thumbs = new ArrayList<PImage>(20);
        selectedIndex = new ArrayList<Integer>(20);
        
        results = new ArrayList<MediaItem>();
        
        group = control.addGroup("fileBrowser").setLabel("").setVisible(false);
        
        lockButtons = new Button[7];
        
        pathField = control.addTextfield("")
            .setText(this.curDir)
            .setPosition(30, 30)
            .setSize(780, 30)
            .setLock(true)
            .setFocus(false)
            .setGroup(group);

        lockButtons[0] = control.addButton("openButton")
            .setCaptionLabel("Open")
            .setPosition(750, 540)
            .setSize(55, 30)
            .setLock(true)
            .setGroup(group);
        lockButtons[0].getCaptionLabel().align(control.CENTER, control.CENTER);

        lockButtons[1] = control.addButton("cancelButton")
            .setCaptionLabel("Cancel")
            .setLock(true)
            .setSize(55, 30)
            .setPosition(815, 540)
            .setGroup(group);
        lockButtons[1].getCaptionLabel().align(control.CENTER, control.CENTER);

        lockButtons[2] = control.addButton("scrollUpButton")
            .setSize(30, 75)
            .setLock(true)
            .setPosition(840, 145)
            .setCaptionLabel("^")
            .setGroup(group);
        lockButtons[2].getCaptionLabel().align(control.CENTER, control.CENTER);

        lockButtons[3] = control.addButton("scrollTopButton")
            .setSize(30, 75)
            .setLock(true)
            .setPosition(840, 70)
            .setCaptionLabel("^\n^")
            .setGroup(group);
        lockButtons[3].getCaptionLabel().align(control.CENTER, control.CENTER);
        
        lockButtons[4] = control.addButton("scrollDownButton")
            .setSize(30, 75)
            .setLock(true)
            .setPosition(840, 380)
            .setCaptionLabel("v")
            .setGroup(group);
        lockButtons[4].getCaptionLabel().align(control.CENTER, control.CENTER);

        lockButtons[5] = control.addButton("scrollBottomButton")
            .setSize(30, 75)
            .setLock(true)
            .setPosition(840, 455)
            .setCaptionLabel("v\nv")
            .setGroup(group);
        lockButtons[5].getCaptionLabel().align(control.CENTER, control.CENTER);

        lockButtons[6] = control.addButton("parentDirButton")
            .setCaptionLabel("..")
            .setLock(true)
            .setGroup(group)
            .setPosition(815, 30)
            .setSize(55, 30);
        lockButtons[6].getCaptionLabel().align(control.CENTER, control.CENTER);

        String label = "Visual (bmp, jpg, png, gif, mov, avi, mpg, mp4)";
        mediaTypeList = control.addDropdownList("mediaTypeList")
            .setCaptionLabel(label)
            .setPosition(30, 570)
            .setSize(710, 30)
            .setBarHeight(30)
            .setGroup(group);
        mediaTypeList.getCaptionLabel().align(control.LEFT, control.CENTER);
        mediaTypeList.addItem(label, 0);
        mediaTypeList.addItem("Audio (mp3, wav, aiff, au, snd)", 1);
        
        pageLabel = control.addButton("pageLabel")
            .setLock(true)
            .setPosition(840, 225)
            .setSize(30, 150)
            .setCaptionLabel("")
            .setGroup(group);
        pageLabel.getCaptionLabel().align(control.CENTER, control.TOP);
        
        changeDir(this.curDir);
    }
    
    /**
     * Callback method for handling ControlP5 UI events.
     * @param e the ControlEvent to handle
     */
    public void controlEvent(ControlEvent e){
        switch(e.getName()) {
        case "scrollUpButton":
            scrollUpButton();
            break;
            
        case "scrollDownButton":
            scrollDownButton();
            break;
            
        case "parentDirButton":
            parentDirButton();
            break;
            
        case "scrollTopButton":
            scrollTopButton();
            break;
            
        case "scrollBottomButton":
            scrollBottomButton();
            break;
            
        case "openButton":
            openButton();

            break;
            
        case "cancelButton":
            cancelButton();
            break;
            
        case "mediaTypeList":
            mediaTypeList(e);
        }
    }
    
    /**
     * ControlP5 UI handler. Changes to parent directory if applicable.
     */
    private void parentDirButton() {
        File file = (new File(curDir)).getParentFile();
        String parentName = "";
        
        if(file != null) {
            parentName = file.getAbsolutePath();
            changeDir(parentName);
        }
        
        if(debug) {
            parent.println("parent button pressed" +
                (file != null ? ("\ncd to " + parentName) : ""));
        }
    }
    
    /**
     * ControlP5 UI handler. Closes the FileBrowser without loading any items.
     */
    private void cancelButton() {
        if(debug) {
            parent.println("cancel button pressed");
        }
        
        selectedIndex.clear();
        results.clear();
        
        toggle(false);
    }
    
    /**
     * ControlP5 UI handler. Enters selected directory or loads selected files.
     */
    private void openButton() {
        if(debug) {
            parent.println("open button pressed" + 
                "\n#selected items: " + selectedIndex.size());
        }
        
        if(selectedIndex.isEmpty()) {
            loadAll();
            
            toggle(false);
        }
        
        else {
            File file = new File(curDir + '/' + 
                fileNames.get(/*curDisplayIndex +*/ selectedIndex.get(0)));
            
            if(file.isDirectory()) {
                changeDir(file.getAbsolutePath());
            }
      
            else {
                if(isAudioMode) {
                    loadAudio();
                }
                
                else {
                    loadVisual();
                }
                
                toggle(false);
            }
        }
    }
    
    /**
     * TODO change thumbnails
     */
    private void updateThumbs() {
    	String fullPath;
    	String[] fileNameParts;
    	Movie movie;
    	PImage thumb;
    	PImage thumb1 = parent.loadImage("data/img/folderThumbNail.png");
    	PImage thumb2 = parent.loadImage("data/img/audioThumbNail.png");
        thumb1.resize(thumbWidth, thumbHeight);
        thumb2.resize(thumbWidth, thumbHeight);
    	
        short j;
        
    	for(int i = curDisplayIndex; i < curDisplayIndex + 20 && i < fileNames.size(); i++) {
    		//
    		if(thumbs.get(i) == null) {
	    		fullPath = curDir + '/' + fileNames.get(i);
	    		
	    		thumb = thumb1;
	    		
	    		//non-directory thumbnail
	    		if(!(new File(fullPath)).isDirectory()) {
	    			if(isAudioMode) {		//audio thumbnail
		    			thumb = thumb2;
		    		}
		    		
		    		else {					//visual thumbnail
		    			fileNameParts = fileNames.get(i).split("\\.");
		    			
		    			for(j = 0; j < imgExt.length; j++) {
		    				if(fileNameParts[fileNameParts.length-1].equalsIgnoreCase(imgExt[j])) {
		    					thumb = parent.loadImage(fullPath);
		    					thumb.resize(thumbWidth, thumbHeight);
		    					
		    					break;
		    				}
		    			}
	
		    			if(j == imgExt.length) {
		    				thumb = parent.createImage(thumbWidth, thumbHeight, parent.RGB);
		                    movie = new Movie(parent, fullPath);
		                    movie.play();
		
		                    if(debug) {
		                    	parent.println("loading movie");
		                    }
		                    while(!movie.available());
		                    if(debug) {
		                        parent.println("getting 1st movie frame");
		                    }
		                    movie.read();
		                    thumb.copy(movie, 0, 0, movie.width, movie.height, 0, 0, thumbWidth, thumbHeight);
		                    movie.stop();
		               
		                    thumbs.set(i, thumb);
		    			}
		    		}
	    		}
	    		
	    		//directory thumbnail
	    		thumbs.set(i, thumb);
    		}
    	}
    }
    
    /**
     * ControlP5 UI handler. Displays the previous page.
     */
    private void scrollUpButton() {
        selectedIndex.clear();
        
        if(curDisplayIndex > 0) {
            curDisplayIndex -= 20;

            updateThumbs();
            
            pageLabel.setCaptionLabel("\n\n\n" + ((curDisplayIndex/20) + 1) +
                "\n\nof\n\n" + ((fileNames.size()/20) + 1));
        }
        
        if(debug) {
            parent.println("scroll up button pressed"
                + "\ncurDisplayIndex: " + curDisplayIndex);
        }
    }
    
    /**
     * ControlP5 UI handler. Displays the first page.
     */
    private void scrollTopButton() {
        selectedIndex.clear();
        
        curDisplayIndex = 0;
        
        updateThumbs();
            
        pageLabel.setCaptionLabel("\n\n\n1\n\nof\n\n" +
            ((fileNames.size()/20) + 1));
        
        if(debug) {
            parent.println("scroll top button pressed"
                + "\ncurDisplayIndex: " + curDisplayIndex);
        }
    }
    
    /**
     * ControlP5 UI handler. Displays the next page.
     */
    private void scrollDownButton() {
        selectedIndex.clear();
        
        if(curDisplayIndex + 20 < fileNames.size()) {
            curDisplayIndex += 20;
            
            updateThumbs();
            
            pageLabel.setCaptionLabel("\n\n\n" + ((curDisplayIndex/20) + 1) +
                "\n\nof\n\n" + ((fileNames.size()/20) + 1));
        }
        
        if(debug) {
            parent.println("scroll down button pressed"
                + "\ncurDisplayIndex: " + curDisplayIndex);
        }
    }
    
    /**
     * ControlP5 UI handler. Displays the last page.
     */
    private void scrollBottomButton() {
        selectedIndex.clear();
        
        curDisplayIndex = (fileNames.size()/20) * 20;
        
        updateThumbs();
        
        pageLabel.setCaptionLabel("\n\n\n" + ((fileNames.size()/20) + 1) + 
            "\n\nof\n\n" + (int)(Math.ceil(fileNames.size()/20.)));
        
        if(debug) {
            parent.println("scroll bottom button pressed"
                + "\ncurDisplayIndex: " + curDisplayIndex);
        }
    }
    
    /**
     * ControlP5 UI handler. Switches the media file type being scanned for.
     * @param e the initiating ControlEvent
     */
    private void mediaTypeList(ControlEvent e) {
        isAudioMode = e.getValue() != 0; 
    
        changeDir(curDir);
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
        String fileName;
        for(imgIndex = 0, i = 0; i < 4; i++) {
            //draw thumbnail rows
            for(j = 0; j < 5 && curDisplayIndex+j+5*i < fileNames.size();
                j++, imgIndex++)
            {
                //draw thumbnail columns
                parent.image(
                    thumbs.get(curDisplayIndex+j+5*i),
                    109 + j*162, 120 + i*115
                );
                
                fileName = fileNames.get(curDisplayIndex+j+5*i);
                
                if(fileName.length() >= 15) {
                    fileName = fileName.substring(0, 14) + "..";
                }
                
                parent.fill(0xffffffff);
                parent.text(fileName, 111 + j*162, 165 + i*115);
                
                parent.noFill();
                if(!selectedIndex.isEmpty() && 
                    selectedIndex.contains((int)imgIndex+curDisplayIndex)) 
                {
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
     */
    private void changeDir(String newDir) {
        thumbs.clear();
        fileNames.clear();
        selectedIndex.clear();
        
        curDir = newDir;

        curDisplayIndex = 0;
        
        File file = new File(curDir);
        
        ArrayList<File> files = new ArrayList<File>(java.util.Arrays.asList(file.listFiles()));
        Iterator<File> fileIter = files.iterator();
        
        String fileName, filePath;
        String[] fileNameParts;
        short i, j;
        PImage thumb = parent.loadImage("data/img/folderThumbNail.png");
        thumb.resize(thumbWidth, thumbHeight);
        
        pathField.setText(file.getAbsolutePath());

        //directories listed first
        j = 0;
        while(fileIter.hasNext()) {
            file = fileIter.next();
            
            if(file.isDirectory()) {
                fileNames.add(file.getName());
                
                if(j < 20) {
                	thumbs.add(thumb);
                	j++;
                }
                
                else {
                	thumbs.add(null);
                }
                
                fileIter.remove();
            }
        }
        
        //list audio files
        if(isAudioMode) {
            thumb = parent.loadImage("data/img/audioThumbNail.png");
            thumb.resize(thumbWidth, thumbHeight);
            
            fileIter = files.iterator();
            while(fileIter.hasNext()) {
                fileName = fileIter.next().getName();
                fileNameParts = fileName.split("\\.");
                
                for(i = 0; i < audioExt.length; i++) {
                    if(fileNameParts[fileNameParts.length-1]
                        .equalsIgnoreCase(audioExt[i]))
                    {
                        fileNames.add(fileName);
                      
                        if(j < 20) {
	                        thumbs.add(thumb);
	                        
	                        j++;
                        }
                        
                        else {
                        	thumbs.add(null);
                        }
                    }
                }
            }
        }

        //list image/video files
        else {
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
                    	if(j < 20) {
	                        thumb = parent.loadImage(filePath);
	                        thumb.resize(thumbWidth, thumbHeight);
	                        thumbs.add(thumb);
	                        
	                        j++;
                    	}
                    	
                    	else {
                    		thumbs.add(null);
                    	}
               
                        fileNames.add(fileName);

                        break;
                    }
                }
                
                //if item wasn't image, it is video
                if(i == imgExt.length) {
                    Movie movie;
                    
                    //create thumbnail of 1st frame of video
                    for(i = 0; i < videoExt.length; i++) {
                        if(fileNameParts[fileNameParts.length-1]
                            .equalsIgnoreCase(videoExt[i]))
                        {
                        	if(j < 20) {
	                            thumb = parent.createImage(thumbWidth, thumbHeight, parent.RGB);
	                            movie = new Movie(parent, filePath);
	                            movie.play();

	                            if(debug) {
	                            	parent.println("loading movie");
	                            }
	                            while(!movie.available());
                                if(debug) {
                                    parent.println("getting 1st movie frame");
                                }
                                movie.read();
                                thumb.copy(movie, 0, 0, movie.width, movie.height, 0, 0, thumbWidth, thumbHeight);
                                movie.stop();
	                       
	                            thumbs.add(thumb);
	                            
	                            j++;
	                        }
                        	
                        	else {
                        		thumbs.add(null);
                        	}
                            
                            fileNames.add(fileName);

                            break;
                        }
                    }
                }
            }
        }
        
        pageLabel.setCaptionLabel("\n\n\n1\n\nof\n\n" +
            ((fileNames.size()/20) + 1));
        
        selectedIndex.clear();
        
        if(dblClick) {
            if(System.currentTimeMillis() - clickTime > 500) {
                dblClick = false;
            }
        }
        
        if(debug) {
            parent.println("#items in directory: " + fileNames.size());
        }
    }
    
    /**
     * Loads the selected audio files.
     */
    private void loadAudio() {
        for(Integer index : selectedIndex) {
            if(curDisplayIndex + index < fileNames.size()) {
                results.add(new AudioItem(minim,
                    curDir + '/' + fileNames.get(curDisplayIndex + index)));
            }
        }
    }
    
    /**
     * Loads the selected visual media files.
     */
    private void loadVisual() {
        String[] fileNameParts;
        String fileName;
        short i;
        for(Integer index : selectedIndex) {
            if(curDisplayIndex + index < fileNames.size()) {
                fileName = fileNames.get(curDisplayIndex + index);
                fileNameParts = fileName.split("\\.");
                
                //file is image
                for(i = 0; i < imgExt.length; i++) {
                    if(fileNameParts[fileNameParts.length-1]
                        .equalsIgnoreCase(imgExt[i]))
                    {
                        results.add(new ImageItem(parent, curDir + '/' + fileName));
    
                        break;
                    }
                }
                
                //file is video
                if(i == videoExt.length) {
                	System.out.println("Adding video to results arraylist");
                    results.add(new MovieItem(parent, curDir + '/' + fileName));
                }
            }
        }
    }
    
    /**
     * Loads all applicable media files in the current directory.
     */
    private void loadAll() {
        File file;
        
        if(isAudioMode) {
            for(String fileName : fileNames) {
                file = new File(curDir + '/' + fileName);
                
                if(file.isFile()) {
                    results.add(new AudioItem(minim, file.getAbsolutePath()));
                }
            }
        }
        
        else {
            String[] fileNameParts;
            short i;
            for(String fileName : fileNames) {
                file = new File(curDir + '/' + fileName);
                
                if(file.isFile()) {
                    fileNameParts = fileName.split("\\.");
                    
                    //file is image
                    for(i = 0; i < imgExt.length; i++) {
                        if(fileNameParts[fileNameParts.length-1]
                            .equalsIgnoreCase(imgExt[i]))
                        {
                            results.add(new ImageItem(parent, curDir + '/'
                                + fileName));

                            break;
                        }
                    }
                    
                    //file is video
                    if(i == imgExt.length) {
                        results.add(new MovieItem(parent, curDir + '/' 
                            + fileName));
                    }
                }
            }
        }
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
                selectedIndex.add(curDisplayIndex+5*row+col);
            }
            
            if(!dblClick) {
                dblClick = true;
                
                clickTime = System.currentTimeMillis();
            }
            
            else {
                dblClick = false;
                
                if(System.currentTimeMillis() - clickTime < 500) {
                    openButton();
                }
            }
                    
            if(debug) {
                parent.println(
                    "mouse clicked: " + mouseX + ',' + mouseY +
                    "\nthumbnail: " + row + ' ' + col +
                    "\nrowSelect: " + rowSelect +
                    "\ncolSelect: " + colSelect +
                    (selectedIndex.isEmpty() ?
                        "" : ", selectedIndex: " + selectedIndex.get(0))
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
        if(isSelecting) {
            int minSelX = (selectBox[0] > selectBox[2] ?
                selectBox[2] : selectBox[0]);
            int maxSelX = (selectBox[0] < selectBox[2] ? 
                selectBox[2] : selectBox[0]);
            int minSelY = (selectBox[1] > selectBox[3] ?
                selectBox[3] : selectBox[1]);
            int maxSelY = (selectBox[1] < selectBox[3] ?
                selectBox[3] : selectBox[1]);
            
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
                        selectedIndex.add(curDisplayIndex+i*5+j);
                    }
                }
            }
            
            if(debug) {
                parent.println(
                    "mouse released: " + mouseX + ' ' + mouseY + 
                    "\nminSel: " + minRow + ' ' + minCol + 
                    "\nmaxSel: " + maxRow + ' ' + maxCol +
                    "\n#items selected: " + selectedIndex.size()
                );
            }
            
            isSelecting = false;
        }
    }
    
    /**
     * Handler for mouse press. Initializes the selection box if applicable.
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void mousePressed(int mouseX, int mouseY) {
        if(mouseX >= 30 && mouseX <= 840  && mouseY >= 70 && mouseY <= 530) {
            selectedIndex.clear();
            
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
     */
    public void toggle(boolean visible) {
        group.setVisible(visible);
        
        for(Button button : lockButtons) {
            button.setLock(!visible);
        }
    }
    
    /**
     * Returns the status of the FileBrowser.
     * @return true if the FileBrowser is visible   
     */
    public boolean isEnabled() {
        return pathField.isVisible();
    }
    
    /**
     * Retrieves the loaded media items. The loaded items are then cleared from
     *   the FileBrowser.
     * @return an ArrayList containing the selected MediaItems
     */
    public ArrayList<MediaItem> getResults() {
        ArrayList<MediaItem> tmp = (ArrayList<MediaItem>) results.clone();
        
        results.clear();
        
        return tmp;
    }
    
    /**
     * Checks if MediaItems have been loaded.
     * @return true if MediaItems have been loaded
     */
    public boolean isReady() {
        return !results.isEmpty();
    }
    
    /**
     * Checks the file type being scanned for.
     * @return true if the FIleBrowser is scanning for audio files 
     */
    public boolean isAudioMode() {
        return isAudioMode;
    }
}
