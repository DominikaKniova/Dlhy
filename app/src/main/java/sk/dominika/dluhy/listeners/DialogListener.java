package sk.dominika.dluhy.listeners;

/**
 * Interface for communicating between activities and dialogs.
 */
public interface DialogListener {

    /**
     * Called when an item is clicked.
     * @param id Id of friend
     */
    void onClick(String id);
}
