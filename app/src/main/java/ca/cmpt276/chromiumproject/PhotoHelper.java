package ca.cmpt276.chromiumproject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import ca.cmpt276.chromiumproject.model.PhotoContainer;

public final class PhotoHelper {
    private static final String TAG = "Catch PhotoManager Exception";

    private PhotoHelper() {
        // private constructor
    }

    // reference: https://stackoverflow.com/questions/56904485/how-to-save-an-image-in-android-q-using-mediastore
    public static void savePhotoAndStoreInModel(Context context, PhotoContainer photoContainer, Bitmap bmp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri uri = savePhotoToMediaStore(context, bmp);
            if (uri != null) {
                photoContainer.setPhotoDataString(uri.toString());
            }
        } else {
            File photoFile = getOutputMediaFile(context);
            savePhotoToExternalStorage(context, photoFile, bmp);
            photoContainer.setPhotoDataString(photoFile.getAbsolutePath());
        }
    }

    public static Bitmap loadPhotoFromModel(Context context, PhotoContainer photoContainer) {
        String photoDataString = photoContainer.getPhotoDataString();
        if (photoDataString == null) {
            return null;
        }

        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri uri = Uri.parse(photoDataString);
            bitmap = getBitmapFromUri(context, uri);
        } else {
            if (photoContainer.hasPhotoFile()) {
                bitmap = getBitmapFromFilePath(photoDataString);
            }
        }

        return bitmap;
    }

    // reference: https://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage
    // Used to store photo to storage for BELOW API ver 29 (Q)
    public static void savePhotoToExternalStorage(Context context, File photoFile, Bitmap bmp) {
        if (photoFile == null) {
            throw new NullPointerException("Error creating media file, check storage permissions: ");
        }
        try {
            FileOutputStream imageOutStream = new FileOutputStream(photoFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, imageOutStream);
            imageOutStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    // reference: https://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage
    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(Context context){
        String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        String fileName = createMediaFileName();
        File photoFile = new File(photoPath, fileName);

        return photoFile;
    }

    // reference: https://medium.com/@vishrut.goyani9/scoped-storage-in-android-writing-deleting-media-files-ee6235d30117
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri savePhotoToMediaStore(Context context, Bitmap bitmap) {
        OutputStream imageOutStream;
        ContentValues contentValues = new ContentValues();

        String mediaFileName = createMediaFileName();

        contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, mediaFileName);
        contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM); // TODO: can also use DIRECTORY_PICTURES?

        ContentResolver contentResolver = context.getContentResolver();
        Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (insert != null) {
            try {
                imageOutStream = contentResolver.openOutputStream(insert);
                if (imageOutStream == null) {
                    throw new IOException("Failed to open output stream.");
                } else if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, imageOutStream)) {
                    imageOutStream.close();
                    return insert;
                } else {
                    throw new IOException("Failed to save bitmap.");
                }
            } catch (IOException e) {
                contentResolver.delete(insert, null, null);
                return insert;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
        return null;
    }

    // ref: https://stackoverflow.com/questions/65210522/how-to-get-bitmap-from-imageuri-in-api-level-30
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        Bitmap bitmap = null;
        try {
            ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
            bitmap = ImageDecoder.decodeBitmap(source);
        }  catch (IOException e) {
            Log.d(TAG, "For some reason image decoder does not work lol");
        }
        return bitmap;
    }
    
    private static String createMediaFileName() {
        String timeStamp = SimpleDateFormat.getDateTimeInstance().format(new Date());
        String fileName ="IMG_"+ timeStamp +".jpg";
        return fileName;
    }

    // reference: https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
    public static Bitmap getBitmapFromFilePath(String filePath) {
        Bitmap bitmap = null;
        try {
            File photoFile = new File(filePath);
            FileInputStream inputStream = new FileInputStream(photoFile);
            bitmap = BitmapFactory.decodeStream(new FileInputStream((photoFile)));
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "For some reason BitmapFactory decodeStream does not work lol");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
