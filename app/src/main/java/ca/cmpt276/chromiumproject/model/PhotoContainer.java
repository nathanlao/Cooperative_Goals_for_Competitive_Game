package ca.cmpt276.chromiumproject.model;

import java.io.File;

/** PhotoContainer is an abstract class used to create objects which contains a reference to a single photo
 *  The photo is stored as a data String. Supports Uri (uri.toString()) and File (file path)
 */

public abstract class PhotoContainer {
    private String photoDataString;

    public boolean hasPhotoFile() {
        if (photoDataString == null) {
            return false;
        }
        File photo = new File(photoDataString);
        return photo.isFile();
    }

    public String getPhotoDataString() {
        return photoDataString;
    }

    public void setPhotoDataString(String photoDataString) {
        this.photoDataString = photoDataString;
    }
}
