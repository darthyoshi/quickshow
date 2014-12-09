/**
 * @file PopupDialogue.java
 * @author Kay Choi
 * @description A popup dialogue for modifying VisualItem parameters in the
 *   slide show.
 */

package quickshow;

import java.util.ArrayList;

import processing.data.StringList;
import quickshow.datatypes.ImageItem;
import quickshow.datatypes.VisualItem;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.Controller;
import controlP5.DropdownList;
import controlP5.Group;
import controlP5.ListBoxItem;
import controlP5.Slider;
import controlP5.Textfield;

@SuppressWarnings("rawtypes")
public class PopupDialogue {
    private boolean debug;
    private Quickshow parent;

    private VisualItem item = null;
    private int tagIndex = -1;
    
    private StringList tags;
    private ArrayList<int[]> tagTimes;
    
    private Group popupGroup;
    private static final int[] popupOrigin = {315, 202};
    private Slider imgDisplaySlider;
    private Button popupAccept, popupCancel;
    private Button tagAdd, tagRemove;
    private Textfield tagField, tagStartField, tagEndField;
    private Controller[] popupLock;
    private DropdownList tagList;
    private static final String tagListLbl = "Add New Caption";
    private int offset;

    /**
     * Class constructor.
     * @param parent the instantiating Quickshow object
     * @param control the ControlP5 object handling UI elements
     */
    public PopupDialogue(Quickshow parent, ControlP5 control) {
        this.parent = parent;

        debug = parent.getDebugFlag();
        
        tags = new StringList();
        tagTimes = new ArrayList<int[]>();

        popupGroup = control.addGroup("popupUI")
            .setLabel("")
            .setVisible(false);

        popupLock = new Controller[8];

        control.addTextarea("tagFieldLabel")
            .setPosition(popupOrigin[0], popupOrigin[1]+20)
            .setText("SET CAPTION TEXT")
            .setGroup(popupGroup);

        control.addTextarea("tagLabel")
            .setPosition(popupOrigin[0], popupOrigin[1]+42)
            .setText("CAPTION TIME")
            .setGroup(popupGroup);

        int[] lblOffset = new int[2];

        lblOffset[0] = 130;
        lblOffset[1] = 40;
        control.addTextarea("tagStartLabel")
            .setPosition(popupOrigin[0] + lblOffset[0] - 41,
                popupOrigin[1] + lblOffset[1] + 2)
            .setText("START")
            .setGroup(popupGroup);
        popupLock[0] = tagStartField = control.addTextfield("tagStartField")
            .setPosition(popupOrigin[0] + lblOffset[0],
                popupOrigin[1] + lblOffset[1])
            .setSize(40, 20)
            .setCaptionLabel("")
            .setAutoClear(false)
            .lock()
            .setGroup(popupGroup);

        lblOffset[0] = 210;
        control.addTextarea("tagEndLabel")
            .setPosition(popupOrigin[0] + lblOffset[0] - 28,
                popupOrigin[1] + lblOffset[1] + 2)
            .setText("END")
            .setGroup(popupGroup);
        popupLock[1] = tagEndField = control.addTextfield("tagEndField")
            .setPosition(popupOrigin[0] + lblOffset[0],
                popupOrigin[1] + lblOffset[1])
            .setSize(40, 20)
            .setCaptionLabel("")
            .setAutoClear(false)
            .lock()
            .setGroup(popupGroup);

        popupLock[2] = tagField = control.addTextfield("tagText")
            .setPosition(popupOrigin[0], popupOrigin[1])
            .setSize(250, 20)
            .setCaptionLabel("")
            .setAutoClear(false)
            .lock()
            .setGroup(popupGroup);

        popupLock[3] = imgDisplaySlider = control.addSlider("Image Display Time")
            .setNumberOfTickMarks(9)
            .setSize(250, 10)
            .setPosition(popupOrigin[0], popupOrigin[1] + 70)
            .setRange(2f, 10f)
            .lock()
            .setValue(-1f)
            .setGroup(popupGroup);
        imgDisplaySlider.getCaptionLabel()
            .align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

        popupLock[4] = popupAccept = control.addButton("Accept")
            .lock()
            .setSize(70, 15)
            .setPosition(popupOrigin[0] + 105, popupOrigin[1] + 105)
            .setGroup(popupGroup);
        popupAccept.getCaptionLabel().alignX(ControlP5Constants.CENTER);

        popupLock[5] = popupCancel = control.addButton("Cancel")
            .lock()
            .setSize(70, 15)
            .setPosition(popupOrigin[0] + 180, popupOrigin[1] + 105)
            .setGroup(popupGroup);
        popupCancel.getCaptionLabel().alignX(ControlP5Constants.CENTER);
        
        popupLock[6] = tagAdd = control.addButton("tagAdd")
            .lock()
            .setCaptionLabel("Add Caption")
            .setSize(122, 15)
            .setPosition(popupOrigin[0], popupOrigin[1] - 25)
            .setGroup(popupGroup);
        tagAdd.getCaptionLabel().alignX(ControlP5Constants.CENTER);
        
        popupLock[7] = tagRemove = control.addButton("tagRemove")
            .lock()
            .setCaptionLabel("Remove Caption")
            .setSize(122, 15)
            .setPosition(popupOrigin[0] + 128, popupOrigin[1] - 25)
            .setGroup(popupGroup);
        tagRemove.getCaptionLabel().alignX(ControlP5Constants.CENTER);
        
        tagList = control.addDropdownList("tagList")
            .actAsPulldownMenu(true)
            .setGroup(popupGroup)
            .setCaptionLabel(tagListLbl)
            .setSize(250, 100)
            .setBarHeight(20)
            .setPosition(popupOrigin[0], popupOrigin[1] - 30);
        tagList.getCaptionLabel().align(ControlP5Constants.LEFT,
            ControlP5Constants.CENTER);
        tagList.addItem(tagListLbl, -1);
    }

    /**
     * Toggles the popup dialogue.
     * @param toggle whether or not to show the popup dialogue
     * @param item the VisualItem being modified
     * @param offset the timeline offset
     */
    public void togglePopup(boolean toggle, VisualItem item, Integer offset) {
        this.item = item;

        if(!toggle || item != null) {
            popupGroup.setVisible(toggle);

            for(Controller popup : popupLock) {
                popup.setLock(!toggle);
            }
        }

        if(item != null) {
            if(item.checkType().equalsIgnoreCase("video")) {
                imgDisplaySlider.setVisible(false);
                imgDisplaySlider.lock();
            }

            else {
                imgDisplaySlider.setValue(item.getDisplayTime());
            }

            this.offset = offset;
            
            populateTags();
        }
        
        else {
            tagList.clear();
            tags.clear();
            tagTimes.clear();
            
            tagList.addItem(tagListLbl, -1);
            
            imgDisplaySlider.setValue(-1f);
            
            tagStartField.setText("");
            tagEndField.setText("");
            tagField.setTab("");
        }
    }

    /**
     * TODO add method header
     */
    private void populateTags() {
        tags = item.getTagTexts().copy();
        tagTimes.addAll(item.getTagTimes());
        
        String tmp;
        for(int i = 0; i < tags.size(); i++) {
            tmp = tags.get(i);
            if(tmp.length() >= 30) {
                tmp = tags.get(i).substring(0, 29)+"..";
            }
            
            tagList.addItem(tags.get(i), i);
        }
        
        tagIndex = -1;
    }

    /**
     * Checks the state of the popup dialogue.
     * @return true if popup dialogue is open
     */
    public boolean isEnabled() {
        return popupGroup.isVisible();
    }

    /**
     * Retrieves the Cartesian coordinates of the top left corner of the popup
     *   dialogue.
     * @return integer array
     */
    public int[] getPopupOrigin() {
        return popupOrigin;
    }

    /**
     * Retrieves the value of the tag Textfield.
     * @return String
     */
    private String getTagText() {
        String result = tagField.getText();

        tagField.setText("");

        return result;
    }

    /**
     * Retrieves the value of the start time Textfield.
     * @return integer
     */
    private int getTagStartTime() {
        int result;

        try {
            result = Math.abs(Integer.parseInt(tagStartField.getText()));
        }

        catch(NumberFormatException e) {
            result = 0;
        }

        tagStartField.setText("");

        return result;
    }

    /**
     * Retrieves the value of the end time Textfield.
     * @return integer
     */
    private int getTagEndTime() {
        int result;

        try {
            result = Math.abs(Integer.parseInt(tagEndField.getText()));
        }

        catch(NumberFormatException e) {
            result = 0;
        }

        tagEndField.setText("");

        return result;
    }

    /**
     * Callback method for drawing the PopupDialogue.
     */
    public void draw() {
        parent.stroke(0);
        parent.fill(0xff3333aa);
        parent.rect(popupOrigin[0]-10, popupOrigin[1]-60, 270, 190);
    }

    /**
     * Callback method for handling ControlP5 UI events.
     * @param event the ControlEvent to handle
     */
    public void controlEvent(ControlEvent event) {
        switch(event.getName()) {
        case "Accept":
            int duration = (int)imgDisplaySlider.getValue();

            if(debug) {
                Quickshow.println("Item duration: " + duration + 's');
            }

            if(item.checkType().equalsIgnoreCase("image")) {
                ((ImageItem)item).setDisplayTime(duration);
            }
            Quickshow.println("passing: "+tags.size());
            item.setTags(tags, tagTimes);

            togglePopup(false, null, null);

            break;

        case "Cancel":
            togglePopup(false, null, null);

            break;
            
        case "tagList":
            tagList((int)event.getValue());
            
            break;
            
        case "tagAdd":
            tagAdd();
            
            break;
            
        case "tagRemove":
            tagRemove();
            
            break;
        }

    }

    /**
     * Removes the current caption from the selected VisualItem.
     */
    private void tagRemove() {
        if(debug) {
            Quickshow.println("Current caption count: " + tags.size());
        }

        if(tagIndex >= 0) {
            tags.remove(tagIndex);
            tagTimes.remove(tagIndex);
            
            tagList.clear();
            tagList.addItem(tagListLbl, -1);
            
            String tmp;
            for(int i = 0; i < tags.size(); i++) {
                tmp = tags.get(i);
                if(tmp.length() >= 30) {
                    tmp = tags.get(i).substring(0, 29)+"..";
                }
                
                tagList.addItem(tags.get(i), i);
            }
            
            tagStartField.setText("");
            tagEndField.setText("");
            
            tagField.setText("");
            
            tagIndex = -1;
        }

        if(debug) {
            Quickshow.println("New caption count: " + tags.size());
        }
        
        tagList.setCaptionLabel(tagListLbl);
    }

    /**
     * Adds a new caption to the selected VisualItem.
     */
    private void tagAdd() {
        String text = getTagText();
        int[] time = {-1, -1};

        if(debug) {
            Quickshow.println("Caption text: " + text +
                "\nCurrent caption count: " + tags.size());
        }

        if(!text.trim().equals("")) {
            time[0] = getTagStartTime() - offset;
            time[1] = getTagEndTime() - offset;
            
            if(time[0] >= 0 && time[1] >= 0) {
                if(tagIndex >= 0) {
                    tags.set(tagIndex, text);
                    tagTimes.set(tagIndex, time);
                }
                
                else {
                    tags.append(text);
                    tagTimes.add(time);
                }
    
                if(text.length() >= 30) {
                    text = text.substring(0, 29) + "..";
                }
                
                for(String blah:tags) {
                    Quickshow.println(blah);
                }
    
                if(tagIndex >= 0) {
                    tagList.getItem(tagIndex).setText(text);
                }
                
                else {
                    tagList.addItem(text, tags.size()-1);
                }
            }
        }
        
        if(debug) {
            Quickshow.println(
                "Caption start: " + (time[0] >= 0 ? time[0] : "N/A") +
                "s\nCaption end: " + (time[1] >= 0 ? time[1] : "N/A") +
                "s\nNew caption count: " + tags.size()
            );
        }
        
        tagList.setCaptionLabel(tagListLbl);
    }

    /**
     * ControlP5 UI handler. Switches the caption being edited.
     * @param index
     */
    private void tagList(int index) {
        if(index >= 0) {
            tagIndex = index;
            
            tagStartField.setText(Integer.toString(tagTimes.get(tagIndex)[0]+offset));
            tagEndField.setText(Integer.toString(tagTimes.get(tagIndex)[1]+offset));
            
            tagField.setText(tags.get(tagIndex));
            
            tagAdd.setCaptionLabel("Edit Caption");
        }
        
        else {
            tagIndex = -1;
            
            tagAdd.setCaptionLabel("Add Caption");
            
            tagStartField.setText("");
            tagEndField.setText("");
            
            tagField.setText("");
        }

        if(debug) {
            Quickshow.println("tag index: " + tagIndex);
        }
    }
}
