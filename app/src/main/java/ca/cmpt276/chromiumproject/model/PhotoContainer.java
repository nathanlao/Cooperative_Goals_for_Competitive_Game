package ca.cmpt276.chromiumproject.model;

import android.graphics.Bitmap;

/** PhotoContainer is an abstract class used to create objects which contain a Bitmap reference to a photo.
 *  Please use deletePhoto() to free up memory when deleting a concrete PhotoContainer object
 */

public abstract class PhotoContainer {
    private Bitmap photo;

    public boolean hasValidPhoto() {
        return photo != null;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void deletePhoto() {
        photo.recycle();
        this.photo = null;
    }
}
