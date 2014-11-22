/**
 * @file ImageItem.java
 * @author Kay Choi
 * @description A wrapper class for image media items. 
 */

package quickshow.datatypes;

import processing.core.PImage;

public class ImageItem extends VisualItem {
    PImage image;

    /**
     * Class constructor.
     * @param parent the Quickshow object
     * @param filename the file name of the image to load
     */
    public ImageItem(quickshow.Quickshow parent, String fileName, PImage thumb) {
        super(parent, fileName, thumb);
        
        image = parent.loadImage(fileName);
    }
    
    /**
     * Retrieves the image.
     * @return a PImage object
     */
    public PImage getImage() {
        return image;
    }
}
