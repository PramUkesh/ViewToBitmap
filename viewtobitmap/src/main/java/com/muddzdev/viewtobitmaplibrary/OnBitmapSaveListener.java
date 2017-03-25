package com.muddzdev.viewtobitmaplibrary;


/**
 * Listener to notify the user when the images is saved and its path
 */
public interface OnBitmapSaveListener {
    /**
     * @param isSaved True if the image is saved, false otherwise
     * @param path    The directory path of the saved image
     */
    void onBitmapSaved(boolean isSaved, String path);
}
