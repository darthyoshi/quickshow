/**
 * @file VisualItem.java
 * @author Kay Choi
 * @description An abstract wrapper class for video and image media items. 
 */

package quickshow.datatypes;

import java.util.ArrayList;

import processing.core.PImage;
import quickshow.Quickshow;

public abstract class VisualItem extends MediaItem {
    private ArrayList<String> annotationTexts;
    private ArrayList<Float[]> annotationTimes;
    private Quickshow parent;
    private PImage thumb;
    
    /**
     * Class constructor.
     * @param parent the Quickshow object
     * @param fileName the file name of the media file to load 
     * @param thumb the media item thumbnail 
     */
    public VisualItem(Quickshow parent, String fileName, PImage thumb) {
        super(fileName);
        
        this.thumb = thumb;
        this.parent = parent;
        
        annotationTexts = new ArrayList<String>();
        annotationTimes = new ArrayList<Float[]>();
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
    public void addAnnotation(String text, float startTime, float stopTime) {
        annotationTexts.add(text);
        
        Float[] times = new Float[2];
        times[0] = startTime;
        times[1] = stopTime;
        annotationTimes.add(times);
    }
    
    /**
     * Modifies an existing annotation.
     * @param index the annotation index
     * @param text the annotation
     * @param startTime the time when the annotation should appear
     * @param stopTime the time when the annotation should disappear
     */
    public void setAnnotation(int index, String text, float startTime, float stopTime) {
        annotationTexts.set(index, text);
        
        Float[] times = new Float[2];
        times[0] = startTime;
        times[1] = stopTime;
        annotationTimes.set(index, times);
    }
    
    /**
     * Retrieves all annotations associated with the VisualItem.
     * @return an ArrayList of Strings 
     */
    public ArrayList<String> getAnnotationTexts() {
        return annotationTexts;
    }
    
    /**
     * Removes all annotations from the VisualItem.
     */
    public void clearAnnotations() {
        annotationTexts.clear();
        annotationTimes.clear();
    }
    
    /**
     * Retrieves all timestamps associated with the VisualItem.
     * @return an ArrayList of arrays of Floats
     */
    public ArrayList<Float[]> getAnnotationTimes() {
        return annotationTimes;
    }
    
    /**
     * Removes a text annotation from the VisualItem.
     * @param index the index of the annotation to remove
     */
    public void removeAnnotation(int index) {
        annotationTexts.remove(index);
        annotationTimes.remove(index);
    }
}
