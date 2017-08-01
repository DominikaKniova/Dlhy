package sk.dominika.dluhy.listeners;

import android.view.View;

import sk.dominika.dluhy.databases_objects.Friend;


public interface DialogListener {

    /**
     * Called when an item is clicked.
     *
     * @param id Id of friend
     */

    void onClick(String id);
}
