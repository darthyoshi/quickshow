/**
 * @file VisualItem.java
 * @author Kay Choi
 * @description An abstract wrapper class for video and image media items. 
 */

package quickshow.datatypes;

import java.util.ArrayList;

import processing.core.PImage;

public abstract class VisualItem extends MediaItem {
    private ArrayList<String> tagTexts;
    private ArrayList<Float[]> tagTimes;
    private PImage thumb;
    protected int displayTime = 0;
    
    /**
     * Class constructor.
     * @param fileName the file name of the media file to load 
     * @param thumb the media item thumbnail 
     */
    public VisualItem(String fileName, PImage thumb) {
        super(fileName);
        
        this.thumb = thumb;
        
        tagTexts = new ArrayList<String>();
        tagTimes = new ArrayList<Float[]>();
        
        //debug tag
/*        Float[] f = new Float[2];
        f[0] = 0f;
        f[1] = 2f;
        tagTimes.add(f);
        tagTexts.add("test: " + fileName);
        
        f = new Float[2];
        f[0] = 3f;
        f[1] = 4f;
        tagTimes.add(f);
        tagTexts.add("test2");
*/
    }
    
    /**
     * Retrieves the thumbnail associated with this MediaItem.
     * @return the thumbnail
     */
    public PImage getThumbnail() {
    	return thumb;
    }    
    
    /**
     * Adds a text annotation to the VisualItem.
     * @param text the annotation
     * @param startTime the time when the annotation should appear
     * @param stopTime the time when the annotation should disappear
     */
    public void addTag(String text, float startTime, float stopTime) {
        tagTexts.add(text);
        
        Float[] times = new Float[2];
        times[0] = startTime;
        times[1] = stopTime;
        tagTimes.add(times);
    }
    
    /**
     * Modifies an existing annotation, or adds a new annotation of the target
     * annotation does not already exist.
     * @param index the annotation index
     * @param text the annotation
     * @param startTime the time when the annotation should appear
     * @param stopTime the time when the annotation should disappear
     */
    public void setTag(int index, String text,
        float startTime, float stopTime)
    {
        if(index < tagTexts.size()) {
            tagTexts.set(index, text);
            
            Float[] times = new Float[2];
            times[0] = startTime;
            times[1] = stopTime;
            tagTimes.set(index, times);
        }
        
        else {
            addTag(text, startTime, stopTime);
        }
    }
    
    /**
     * Retrieves all annotations associated with the VisualItem.
     * @return an ArrayList of Strings 
     */
    public ArrayList<String> getTagTexts() {
        return tagTexts;
    }
    
    /**
     * Removes all annotations from the VisualItem.
     */
    public void clearTags() {
        tagTexts.clear();
        tagTimes.clear();
    }
    
    /**
     * Retrieves all timestamps associated with the VisualItem.
     * @return an ArrayList of arrays of Floats
     */
    public ArrayList<Float[]> getTagTimes() {
        return tagTimes;
    }
    
    /**
     * Removes a text annotation from the VisualItem.
     * @param index the index of the annotation to remove
     */
    public void removeTag(int index) {
        tagTexts.remove(index);
        tagTimes.remove(index);
    }
    
    /**
     * Retrieves the time that the VisualItem will be displayed. 
     * @return the time in seconds
     */
    public int getDisplayTime() {
    	return displayTime;
    }
}
