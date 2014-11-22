/**
 * @file MediaItem.java
 * @author Kay Choi
 * @description An abstract wrapper class for all media items. 
 */

package quickshow.datatypes;

public abstract class MediaItem {
    private String fileName;
  
    private static final String[] imgExt = {
        "bmp", "jpg", "png", "gif" 
    };
    
    private static final String[] audioExt = {
        "mp3", "wav", "aiff", "au", "snd"
    };
    
    /**
     * Class constructor.
     * @param fileName the file name of the media file to load
     */
    public MediaItem(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Retrieves the file name of the media item.
     * @return the name of the media file
     */
    public String getFileName() {
    	return fileName;
    }
    
    /**
     * Retrieves the media type of this VIsualItem.
     * @return the item media type
     */
    public String checkType() {
        String result = null;
        
        short i;
        
        String[] fileNameParts = fileName.split("\\.");
        
        for(i = 0; i < audioExt.length; i++) {
            if(fileNameParts[fileNameParts.length-1]
                .equalsIgnoreCase(audioExt[i]))
            {
                result = "audio";
                break;
            }
        }
        
        if(i == audioExt.length) {
            for(i = 0; i < imgExt.length; i++) {
                if(fileNameParts[fileNameParts.length-1]
                    .equalsIgnoreCase(imgExt[i]))
                {
                    result = "image";
                    break;
                }
            }
            
            if(i == imgExt.length) {
                result = "video";
            }
        }
        
        return result;
    }
}
