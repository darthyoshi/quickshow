package quickshow.datatypes;

public abstract class MediaItem {
    String fileName;

    private static final String[] imgExt = {
        "bmp", "jpg", "png", "gif" 
    };
    
    private static final String[] audioExt = {
        "mp3", "wav", "aiff", "au", "snd"
    };
    
    public MediaItem(String fileName) {
        this.fileName = fileName;
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
