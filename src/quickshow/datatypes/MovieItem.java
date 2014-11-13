package quickshow.datatypes;

import quickshow.Quickshow;
import processing.video.*;

public class MovieItem extends VisualItem {
    Movie movie;
    
    /**
     * Class constructor.
     * @param parent the Quickshow object
     * @param filename the file name of the video to load
     */
    public MovieItem(Quickshow parent, String filename) {
        super(parent);
        
        movie = new Movie(parent, filename);
    }
    
    /**
     * Retrieves the video.
     * @return a Movie object
     */
    public Movie getMovie() {
        return movie;
    }

}
