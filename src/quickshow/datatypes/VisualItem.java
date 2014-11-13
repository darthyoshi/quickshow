package quickshow.datatypes;

import java.util.*;
import processing.video.*;
import processing.core.*;
import quickshow.*;

public abstract class VisualItem {
    ArrayList<String> annotationTexts;
    ArrayList<Integer[]> annotationTimes;
    Quickshow parent;
    
    /**
     * Class constructor.
     * @param parent the Quickshow object 
     */
    public VisualItem(Quickshow parent) {
        this.parent = parent;
        
        annotationTexts = new ArrayList<String>();
        annotationTimes = new ArrayList<Integer[]>();
    }
    
    /**
     * Adds a text annotation to the VisualItem.
     * @param text the annotation
     * @param startTime the time when the annotation should appear
     * @param stopTime the time when the anootation should disappear
     */
    void addAnnotation(String text, int startTime, int stopTime) {
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
    void removeAnnotation(int index) {
        annotationTexts.remove(index);
        annotationTimes.remove(index);
    }

}
