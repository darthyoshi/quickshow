/**
 * @file VisualItem.java
 * @author Kay Choi
 * @description An abstract wrapper class for video and image media items. 
 */

package quickshow.datatypes;

import java.util.ArrayList;
import quickshow.Quickshow;

public abstract class VisualItem extends MediaItem {
    ArrayList<String> annotationTexts;
    ArrayList<Integer[]> annotationTimes;
    Quickshow parent;
    
    /**
     * Class constructor.
     * @param parent the Quickshow object
     * @param fileName the file name of the media item to load  
     */
    public VisualItem(Quickshow parent, String fileName) {
        super(fileName);
        
        this.parent = parent;
        
        annotationTexts = new ArrayList<String>();
        annotationTimes = new ArrayList<Integer[]>();
    }
    
    /**
     * Adds a text annotation to the VisualItem.
     * @param text the annotation
     * @param startTime the time when the annotation should appear
     * @param stopTime the time when the annotation should disappear
     */
    public void addAnnotation(String text, int startTime, int stopTime) {
        annotationTexts.add(text);
        
        Integer[] times = new Integer[2];
        times[0] = startTime;
        times[1] = stopTime;
        annotationTimes.add(times);
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
