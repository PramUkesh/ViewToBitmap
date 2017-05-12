package com.muddzdev.viewtobitmaplibrary;

/**
 * Listener to notify the user if the images has been successfully saved and with its path
 */
public interface OnSaveResultListener {
    /**
     * @param isSaved True if the image has been successfully saved, false otherwise
     * @param path    The path of the saved image
     */
    void onSaveResult(boolean isSaved, String path);
}
