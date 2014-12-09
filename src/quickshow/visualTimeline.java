/**
 * @file visualTimeline.java
 * @author Moses Lee, Kay Choi
 * @description Renders the Quickshow visual item timeline.
 */

package quickshow;

import java.util.ArrayList;
import java.util.ListIterator;

import processing.core.PConstants;
import processing.core.PImage;
import quickshow.datatypes.VisualItem;

public class visualTimeline {
    private final static int timeLineWidth = 840;
    private final static int timeLineHeight = 78;
    private final static int MAX_THUMBNAIL_DISPLAY = 8;
    private final static int WIDTH_PER_SEC = 15;
    private int start_index = 0;
    private float scaleFactor;
    private Quickshow parent;
    private boolean debug;
    private int num_pages = 0;
    private int curr_page_index = 0;
    private int curr_items_displayed = 0;
    private int prev_items_displayed = 0;
    private int totalTime = 0;
    private int curr_Time = 0;
    private int curr_img_length = 0;
    private int overall_img_length = 0;
    private int selectedIndex = -1;
    private ArrayList<Integer[]> timeStamps;
    private ArrayList<Integer[]> timeLineBounds;
    final static int[] bounds = {30, 499, 870, 577};

    private ArrayList <VisualItem> itemsForDisplay;

    /**
     * Class constructor.
     * @param parent the instantiating Quickshow object
     */
    public visualTimeline(Quickshow parent){
        this.parent = parent;
        itemsForDisplay = new ArrayList<VisualItem>();
        timeStamps = new ArrayList<Integer[]>();
        timeLineBounds = new ArrayList<Integer[]>();
        debug = parent.getDebugFlag();
    }

    /**
     * Drawing a simple background canvas
     */
    public void drawBackgroundCanvas() {
        parent.rectMode(PConstants.CORNER);
        parent.imageMode(PConstants.CENTER);

        parent.fill(90,90,90);
        parent.stroke(0);
        parent.rect(bounds[0], bounds[1], timeLineWidth, timeLineHeight);
    /*
        parent.stroke(0xffffffff);
        short x;
        for(short i = 1; i < 30; i++) {
            x = (short)(i*28 + 30);

            parent.stroke((i%5 == 0 ? 0xff55aaff : 0xffffffff));
            parent.line(x, bounds[1]+2, x, bounds[3]-2);
        }*/
    }

    /**
     * Generates thumbnails for the selected items.
     */
    public void generateThumbnails() {
        if(start_index < itemsForDisplay.size()) {
            ListIterator<VisualItem> itemIter = itemsForDisplay
                .listIterator(start_index);

            //If empty exit function
            if(itemIter.hasNext()) {
                parent.imageMode(PConstants.CORNER);
                PImage image;

                int drawIndex = bounds[0] + 2;
                int y, duration;
                float new_height, new_width;
                int width_by_sec;
                int j = start_index;
                VisualItem item;

                do {
                    item = itemIter.next();
                    image = item.getThumbnail();

                    //Resize image to fit on timeline w/ Aspect Ratio
                    if (image.height > timeLineHeight){
                        scaleFactor = 1.0f/((float) image.height/
                            (float) (timeLineHeight-15));
                    }

                   new_height = scaleFactor * image.height;
                   new_width = scaleFactor * image.width;

                    y = (bounds[3]+bounds[1]-(int)Math.ceil(new_height))/2;

                    duration = item.getDisplayTime();
                    
                    width_by_sec = item.getDisplayTime() * WIDTH_PER_SEC;
                    
                    if(new_width > width_by_sec) {
                    	new_width = width_by_sec - 1; 
                    }
                    
                    parent.rectMode(PConstants.CORNER);
                    parent.fill(0xff40E0D0);
                    parent.stroke(0x00000000);
                    parent.rect(drawIndex, bounds[1], width_by_sec, timeLineHeight);
                    
                    parent.image(image, drawIndex+1, y, new_width,
                        new_height);

                    if(selectedIndex == j) {
                        parent.fill(0x55ff3210);
                        parent.stroke(0xffff2233);
                        parent.rectMode(PConstants.CORNER);
                        parent.rect(drawIndex, bounds[1], width_by_sec,
                            timeLineHeight);
                    }

                    //Increment the x index
                    drawIndex += width_by_sec+1;
                    curr_img_length = drawIndex;

                    //Get the duration of the currently drawn images
                    if(curr_Time < totalTime) curr_Time += duration;

                    j++;
                } while(itemIter.hasNext() &&
                    drawIndex + width_by_sec <= timeLineWidth);
            }
        }
    }

    /**
     * Adds the selected visual items to the timeline.
     * @param selectedList an ArrayList containing the selected VisualItems
     */
    public void receiveSelectedItems(ArrayList<VisualItem> selectedList){
        itemsForDisplay.clear();
        timeStamps.clear();
        timeLineBounds.clear();
        totalTime = 0;

        int tmp;
        Integer[] times;
        for(VisualItem item : selectedList) {
            itemsForDisplay.add(item);

            tmp = item.getDisplayTime();
            totalTime += tmp;
            times = new Integer[2];
            times[0] = totalTime - tmp;
            times[1] = totalTime;
            timeStamps.add(times);
        }

        curr_Time = 0;
        calculateTimeLineBounds(0);

        num_pages = itemsForDisplay.size()/MAX_THUMBNAIL_DISPLAY + 1;
        curr_page_index = 1;
    }

    /**
     * Calculates the start and end timestamps for each VisualItem.
     * @param start the index of the first VisualItem to process
     */
    public void calculateTimeLineBounds(int start){
        //Get initial draw index
        if(start < itemsForDisplay.size()) {
            ListIterator<VisualItem> itemIter = itemsForDisplay.listIterator(start);

            if(itemIter.hasNext()) {
                VisualItem item;
                PImage image;

                int drawIndex = bounds[0];
                curr_items_displayed = 0;

                float time_scaled_width;
                float width_by_sec;

                Integer[] tbBounds, curBounds;
                ListIterator<Integer[]> boundIter = timeLineBounds
                    .listIterator(start);

                //Go through the list and calculate placements along the time line
                do {
                    item = itemIter.next();
                    image = item.getThumbnail();

                    //Adjust each image to fit on timeline maintaining Aspect Ratio
                    if (image.height > timeLineHeight){
                        scaleFactor = 1.0f/((float) image.height/
                            (float) (timeLineHeight-15));
                    }

                    time_scaled_width = scaleFactor * image.width;
                    width_by_sec = item.getDisplayTime() * WIDTH_PER_SEC;
                    
                    if(time_scaled_width > width_by_sec) {
                    	time_scaled_width = width_by_sec; 
                    }
                    drawIndex += width_by_sec+1;
                    curr_img_length = drawIndex;

                    tbBounds = new Integer[2];
                    tbBounds[0] = (int) (drawIndex - width_by_sec);
                    tbBounds[1] = drawIndex;

                    if(boundIter.hasNext()) {
                        curBounds = boundIter.next();
                        curBounds[0] = tbBounds[0];
                        curBounds[1] = tbBounds[1];
                    }

                    else {
                        boundIter.add(tbBounds);
                    }

                    //Check to see if the image can be drawn in timeline
                    if(drawIndex + width_by_sec > timeLineWidth) break;

                    //track of how many items are on the visualtimeline
                    curr_items_displayed++;
                } while(itemIter.hasNext());

                if(debug) {
                    Quickshow.println("curr_Items display: " + curr_items_displayed);
                }
                overall_img_length += curr_img_length;
            }
        }
    }

    /**
     * Removes all visual items from the timeline.
     */
    public void clearSelectedSlides(){
        timeLineBounds.clear();
        timeStamps.clear();

        ListIterator<VisualItem> iter = itemsForDisplay.listIterator();
        VisualItem item;
        while(iter.hasNext()) {
            item = iter.next();
            item.clearTags();

            if(item.checkType().equalsIgnoreCase("image")) {
                ((quickshow.datatypes.ImageItem)item).setDisplayTime(5);
            }

            iter.remove();
        }

        //Reset the display index
        start_index = 0;
        curr_page_index = 0;
        num_pages = 0;
        totalTime = 0;
        curr_items_displayed = 0;
    }

    /**
     * Goes to the next page on the timeline.
     */
    public void showNextOnTimeline(){
        start_index = start_index + curr_items_displayed + 1;
        if(start_index >= itemsForDisplay.size()) start_index = 0;
        prev_items_displayed = curr_items_displayed;
        calculateTimeLineBounds(start_index);

        //wrap page index
        curr_page_index = (++curr_page_index) > num_pages ? 1 : curr_page_index;
    }

    /**
     * Goes to the previous page on the timeline.
     * TODO make sure to wrap around back to the end of the time line
     */
    public void showPrevOnTimeline(){
        start_index = start_index - prev_items_displayed - 1;
        if(start_index < 0) {
            start_index = 0;
            //start_index = itemsForDisplay.size() - 1;
        }
        calculateTimeLineBounds(start_index);

        //wrap page index
        curr_page_index = (--curr_page_index) < 1 ? num_pages : curr_page_index;
    }

    /**
     * Returns the total length of time for the slide show
     */
    public int getTotalTime(){
        return totalTime;
    }

    /**
     * Displays the marker in the visual timeline and the thumbnail
     *   that the image is hovering over.
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    public void displayTimeMarker(int x, int y){
        int index = getTimelineIndex(x, y);

        //If legal index was found then generate the marker and preview thumbnail
        if(index > -1) {
            PImage prevThumbnail = itemsForDisplay.get(index).getThumbnail();

            parent.fill(0xff555555);
            parent.stroke(0);
            parent.rectMode(PConstants.CORNER);
            
            int x_coord = x < 450 ? x : x-prevThumbnail.width;
            
            parent.rect(x_coord, bounds[1]-60, prevThumbnail.width, prevThumbnail.height);

            parent.image(prevThumbnail, x_coord, bounds[1]-60);
            parent.stroke(0xffff0000);
            parent.line(x, bounds[1] + 2 , x, bounds[3] - 2);


            Integer[] stamp = timeStamps.get(index);
            int min = stamp[0]/60;
            int sec = stamp[0]%60;
            String text = String.format("%d:%02d", min, sec);

            parent.fill(0xffff0055);
            parent.textAlign(PConstants.RIGHT);
            parent.text(text, x_coord-5, bounds[1]);

            min = stamp[1]/60;
            sec = stamp[1]%60;
            text = String.format("%d:%02d", min, sec);

            parent.textAlign(PConstants.LEFT);
            parent.text(text, x_coord+prevThumbnail.width+5, bounds[1]);
        }
    }

    /**
     * Retrieves the VisualItem at the specified index.
     * @param index the index of the VisualItem
     * @return VisualItem
     * @return null if index is out of bounds
     */
    public VisualItem getItemAt(int index) {
        return (index < 0 || index >= itemsForDisplay.size()) ?
            null : itemsForDisplay.get(index);
    }

    /**
     * Retieves the timestamps of the VisualItem at the specified index.
     * @param index the index of the VisualItem
     * @return Integer array containing the start and stop times
     * @return null if index is out of bounds
     */
    public Integer[] getItemTimeStamps(int index) {
        return (index < 0 || index >= timeStamps.size()) ?
            null : timeStamps.get(index);
    }

    /**
     * Determines the index of the VisualItem at the mouse pointer.
     * @param mouseX the x-coordinates of the mouse
     * @param mouseY the y-coordinates of the mouse
     * @return integer
     */
    public int getTimelineIndex(int mouseX, int mouseY) {
        int index = -1;

        if(start_index < timeLineBounds.size()) {
            ListIterator<Integer[]> boundIter = timeLineBounds
                .listIterator(start_index);
            
            Integer[] tBound;

            if(boundIter.hasNext()) {
                //Get relative to current pixel length
                float timeScale = (float) mouseX/curr_img_length;
                //Locate where in timeline to show preview
                float where = (timeScale * curr_img_length);
                int i = start_index;

                //Find the image that falls within the bounds
                do {
                    tBound = boundIter.next();

                    if(where >= tBound[0] &&
                        where < tBound[1])
                    {
                        index = i;
                        break;
                    }

                    i++;
                } while(boundIter.hasNext());
            }
        }

        return index;
    }

    /**
     * Updates the VisualItem timestamps.
     * @param index the index of the edited VisualItem
     */
    public void updateTimeStamps(int index) {
        if(debug) {
            Quickshow.println(""+itemsForDisplay.size()+' '+timeStamps.size());
        }

        if(index < itemsForDisplay.size()) {
            ListIterator<VisualItem> itemIter = itemsForDisplay.listIterator();

            if(itemIter.hasNext()) {
                int i = 0, tmp;
                Integer[] stamps;

                totalTime = 0;

                do {
                    totalTime += itemIter.next().getDisplayTime();
                    i++;
                } while(itemIter.hasNext() && i < index);

                ListIterator<Integer[]> stampIter = timeStamps.listIterator(i);
                while(itemIter.hasNext()) {
                    tmp = itemIter.next().getDisplayTime();

                    stamps = stampIter.next();

                    stamps[0] = totalTime;
                    totalTime += tmp;
                    stamps[1] = totalTime;
                }
            }
        }
    }


    /**
     * Designates an item on the timeline as selected.
     * @param index the index of the VisualItem to select
     */
    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    /**
     * Retrieves the index of the first visible item on the timeline.
     * @return integer
     */
    public int getStartIndex(){
        return start_index;
    }

    /**
     * Retrieves the timestamps of the currently displayed timeline items.
     * @return integer array
     */
    public int[] getCurPageStamps() {
        int[] result = {0, 0};
        if(!timeStamps.isEmpty()) {
            result[0] = timeStamps.get(start_index)[0];
            result[1] = timeStamps.get(curr_items_displayed+start_index-1)[1];
        }

        return result;
    }
}
