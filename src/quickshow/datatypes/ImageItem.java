package quickshow.datatypes;

import processing.core.*;
import quickshow.*;

public class ImageItem extends VisualItem {
    PImage image;

    /**
     * Class constructor.
     * @param parent the Quickshow object
     * @param filename the file name of the image to load
     */
    public ImageItem(Quickshow parent, String filename) {
        super(parent);
        
        image = parent.loadImage(filename);
    }
    
    /**
     * Retrieves the image.
     * @return a PImage object
     */
    public PImage getImage() {
        return image;
    }
}
