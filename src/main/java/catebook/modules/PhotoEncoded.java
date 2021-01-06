
package catebook.modules;

public class PhotoEncoded {
    private String encodedString;
    private String photoDescription;
    private Long photoId;
    
    public PhotoEncoded(String code, Long id) {
        encodedString = code;
        photoId = id;
    }

    public String getEncodedString() {
        return encodedString;
    }

    public void setEncodedString(String encoded) {
        this.encodedString = encoded;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
}
