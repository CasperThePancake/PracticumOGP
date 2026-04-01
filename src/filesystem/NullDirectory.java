package filesystem;

/**
 * A placeholder null directory class for OGP Practicum
 *
 * @note Used as the placeholder "mother of all directories", is the parentDirectory for root folders! Inaccessible to the user.
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
class NullDirectory extends Directory {
    // =================================================================================
    // Constructors
    // =================================================================================
    NullDirectory() {
        super(true);
    }

    // =================================================================================
    // Other methods
    // =================================================================================
    void addItem(Item item) {
        // Prevent things from actually being stored
    }

    void removeItem(Item item) {
        // Prevent things from actually being stored
    }
}
