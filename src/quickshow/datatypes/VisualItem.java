/**
 * @file VisualItem.java
 * @author Kay Choi
 * @description An abstract wrapper class for video and image media items.
 */

package quickshow.datatypes;

import java.util.ArrayList;

import processing.core.PImage;
import processing.data.StringList;

public abstract class VisualItem extends MediaItem {
    private StringList tagTexts;
    private ArrayList<int[]> tagTimes;
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

        tagTexts = new StringList();
        tagTimes = new ArrayList<int[]>();

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
     * Retrieves all annotations associated with the VisualItem.
     * @return StringList
     */
    public StringList getTagTexts() {
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
    public ArrayList<int[]> getTagTimes() {
        return tagTimes;
    }

    /**
     * Retrieves the time that the VisualItem will be displayed.
     * @return the time in seconds
     */
    public int getDisplayTime() {
        return displayTime;
    }

    /**
     * TODO add method header
     * @param tags
     * @param tagTimes
     */
    public void setTags(StringList tags, ArrayList<int[]> tagTimes) {
        clearTags();
        
        tagTexts = tags.copy();
        this.tagTimes.addAll(tagTimes);
    }
}
