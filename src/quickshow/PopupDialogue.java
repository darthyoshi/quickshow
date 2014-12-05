/**
 * @file PopupDialogue.java
 * @author Kay Choi
 * @description A popup dialogue for modifying VisualItem parameters in the
 *   slide show.
 */

package quickshow;

import quickshow.datatypes.ImageItem;
import quickshow.datatypes.VisualItem;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.Controller;
import controlP5.Group;
import controlP5.Slider;
import controlP5.Textfield;

public class PopupDialogue {
    private boolean debug;

    private ControlP5 controlp5;
    private Quickshow parent;

    private VisualItem item = null;

    private Group popupGroup;
    private static final int[] popupOrigin = {350, 200};
    private Slider imgDisplaySlider;
    private Button popupAccept, popupCancel;
    private Textfield tagField, tagStartField, tagEndField;
    private Controller[] popupLock;
    private int offset;

    /**
     * Class constructor.
     * @param parent the instantiating Quickshow object
     * @param control
     */
    public PopupDialogue(Quickshow parent, ControlP5 control) {
        this.parent = parent;
        this.controlp5 = control;

        debug = parent.getDebugFlag();

        popupGroup = controlp5.addGroup("popupUI")
            .setLabel("")
            .setVisible(false);

        popupLock = new Controller[6];

        controlp5.addTextarea("tagFieldLabel")
            .setPosition(popupOrigin[0], popupOrigin[1]+20)
            .setText("SET CAPTION TEXT")
            .setGroup(popupGroup);

        controlp5.addTextarea("tagLabel")
            .setPosition(popupOrigin[0], popupOrigin[1]+42)
            .setText("CAPTION TIME")
            .setGroup(popupGroup);

        int[] lblOffset = new int[2];

        lblOffset[0] = 130;
        lblOffset[1] = 40;
        controlp5.addTextarea("tagStartLabel")
            .setPosition(popupOrigin[0] + lblOffset[0] - 41,
                popupOrigin[1] + lblOffset[1] + 2)
            .setText("START")
            .setGroup(popupGroup);
        popupLock[0] = tagStartField = controlp5.addTextfield("tagStartField")
            .setPosition(popupOrigin[0] + lblOffset[0],
                popupOrigin[1] + lblOffset[1])
            .setSize(40, 20)
            .setCaptionLabel("")
            .setAutoClear(false)
            .lock()
            .setGroup(popupGroup);

        lblOffset[0] = 210;
        controlp5.addTextarea("tagEndLabel")
            .setPosition(popupOrigin[0] + lblOffset[0] - 28,
                popupOrigin[1] + lblOffset[1] + 2)
            .setText("END")
            .setGroup(popupGroup);
        popupLock[1] = tagEndField = controlp5.addTextfield("tagEndField")
            .setPosition(popupOrigin[0] + lblOffset[0],
                popupOrigin[1] + lblOffset[1])
            .setSize(40, 20)
            .setCaptionLabel("")
            .setAutoClear(false)
            .lock()
            .setGroup(popupGroup);

        popupLock[2] = tagField = controlp5.addTextfield("tagText")
            .setPosition(popupOrigin[0], popupOrigin[1])
            .setSize(250, 20)
            .setCaptionLabel("")
            .setAutoClear(false)
            .lock()
            .setGroup(popupGroup);

        popupLock[3] = imgDisplaySlider = controlp5.addSlider("Image Display Time")
            .setNumberOfTickMarks(9)
            .setSize(250, 10)
            .setPosition(popupOrigin[0], popupOrigin[1] + 70)
            .setRange(2f, 10f)
            .lock()
            .setValue(5f)
            .setGroup(popupGroup);
        imgDisplaySlider.getCaptionLabel()
            .align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

        popupLock[4] = popupAccept = controlp5.addButton("Accept")
            .lock()
            .setSize(70, 15)
            .setPosition(popupOrigin[0] + 105, popupOrigin[1] + 105)
            .setGroup(popupGroup);
        popupAccept.getCaptionLabel().alignX(ControlP5Constants.CENTER);

        popupLock[5] = popupCancel = controlp5.addButton("Cancel")
            .lock()
            .setSize(70, 15)
            .setPosition(popupOrigin[0] + 180, popupOrigin[1] + 105)
            .setGroup(popupGroup);
        popupCancel.getCaptionLabel().alignX(ControlP5Constants.CENTER);
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
        }
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
        parent.rect(popupOrigin[0]-10, popupOrigin[1]-10, 270, 140);
    }

    /**
     * Callback method for handling ControlP5 UI events.
     * @param event the ControlEvent to handle
     */
    public void controlEvent(ControlEvent event) {
        switch(event.getName()) {
        case "Accept":
            String text = getTagText();
            int startTime = -1;
            int endTime = -1;

            if(debug) {
                Quickshow.println("Caption text: " + text);
            }

            if(!text.trim().equals("")) {
                startTime = getTagStartTime();
                endTime = getTagEndTime();
                item.setTag(0, text, startTime - offset, endTime - offset);
            }

            int duration = (int)imgDisplaySlider.getValue();

            if(debug) {
                Quickshow.println(
                    "Caption start: " + (startTime >= 0 ? startTime : "N/A") +
                    "s\nCaption end: " + (endTime >= 0 ? endTime : "N/A") +
                    "s\nImage duration: " + duration
                );
            }

            if(item.checkType().equalsIgnoreCase("image")) {
                ((ImageItem)item).setDisplayTime(duration);
            }

            togglePopup(false, null, null);

            break;

        case "Cancel":
            togglePopup(false, null, null);

            break;
        }

    }
}
