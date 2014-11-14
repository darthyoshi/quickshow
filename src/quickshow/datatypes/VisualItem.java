package quickshow.datatypes;

import java.util.ArrayList;
import quickshow.Quickshow;

public abstract class VisualItem {
    ArrayList<String> annotationTexts;
    ArrayList<Integer[]> annotationTimes;
    Quickshow parent;
    String fileName;

    private final String[] imgExt = {
        "bmp", "jpg", "png", "gif" 
    };
    
    /**
     * Class constructor.
     * @param parent the Quickshow object
     * @param fileName the file name of the media item to load  
     */
    public VisualItem(Quickshow parent, String fileName) {
        this.fileName = fileName;
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
    
    /**
     * Retrieves the media type of this VIsualItem.
     * @return the item media type
     */
    public String checkType() {
        short i;
        
        String[] fileNameParts = fileName.split("\\.");
        for(i = 0; i < imgExt.length; i++) {
            if(fileNameParts[fileNameParts.length-1]
                .equalsIgnoreCase(imgExt[i]))
            {
                break;
            }
        }
        
        return (i < imgExt.length ? "image" : "video");
    }

}
