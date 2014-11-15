/**
 * @file MovieItem.java
 * @author Kay Choi
 * @description A wrapper class for video media items. 
 */

package quickshow.datatypes;

import processing.video.Movie;

public class MovieItem extends VisualItem {
    Movie movie;
    
    /**
     * Class constructor.
     * @param parent the Quickshow object
     * @param fileName the file name of the video to load
     */
    public MovieItem(quickshow.Quickshow parent, String fileName) {
        super(parent, fileName);
        
        movie = new Movie(parent, fileName);
    }
    
    /**
     * Retrieves the video.
     * @return a Movie object
     */
    public Movie getMovie() {
        return movie;
    }
}
