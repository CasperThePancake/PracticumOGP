package filesystem;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic directory class for OGP Practicum
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Directory extends Item {
    // =================================================================================
    // Attributes
    // =================================================================================
    private List<Item> items = new ArrayList<>();
    static final Directory NULL_ROOT = new NullDirectory();

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create an internal directory placeholder
     *
     * @param internal Whether this directory is for internal use only
     *
     * @note Used for creating NULL_ROOT placeholder as parentDirectory for root directories
     * @note This method is package-private to prevent users from using it
     */
    Directory(boolean internal) {
        super(internal);
    }

    /**
     * Create a directory with given parentDirectory, name, and writability
     *
     * @effect Parent directory is set to given parentDirectory
     *      | move(parentDirectory)
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @effect Writability is set to given writability
     *      | setWritable(writable)
     *
     * @param parentDirectory The parent directory for the directory
     * @param name The name for the directory
     * @param writable The writability for the directory
     */
    public Directory(Directory parentDirectory, String name, boolean writable) {
        super(name, writable, parentDirectory);
    }

    /**
     * Create a writable directory with given parentDirectory and name
     *
     * @effect Parent directory is set to given parentDirectory
     *      | move(parentDirectory)
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @effect Writability is set to true
     *      | setWritable(true)
     *
     * @param parentDirectory The parent directory for the directory
     * @param name The name for the directory
     */
    public Directory(Directory parentDirectory, String name) {
        super(name, true, parentDirectory);
    }

    /**
     * Create a root directory with given name and writability
     *
     * @post The directory has no parent directory
     *      | getParentDirectory() == null
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @effect Writability is set to given writability
     *      | setWritable(writable)
     *
     * @param name The name for the directory
     * @param writable The writability for the directory
     */
    public Directory(String name, boolean writable) {
        super(name, writable, NULL_ROOT);
    }

    /**
     * Create a writable root directory with given name
     *
     * @post The directory has no parent directory
     *      | getParentDirectory() == null
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @effect Writability is set to true
     *      | setWritable(true)
     *
     * @param name The name for the directory
     */
    public Directory(String name) {
        super(name, true, NULL_ROOT);
    }

    // =================================================================================
    // Root directories
    // =================================================================================

    /**
     * Make this directory a root directory
     *
     * @post This directory has no parent directory
     *      | new.getParentDirectory() == null
     *
     * @effect This directory is moved to the root level
     *      | move(null)
     */
    public void makeRoot() {
        this.move(NULL_ROOT);
    }

    /**
     * Check whether this directory is a root directory
     *
     * @return True if this directory is a root directory; false otherwise
     *      | getParentDirectory() == null
     */
    public boolean isRoot() {
        return this.getParentDirectory() == null; // Remember that the getter returns null instead of NULL_ROOT to prevent user access
    }

    // No removeRoot(), you can just put the folder into a real folder to make it no longer root!

    // =================================================================================
    // Items
    // =================================================================================

    // Add and remove here are package-private: the user cannot run them, other classes CAN, because this can only be called by Item's move() method
    // In other words: Item keeps track of the bidirectional association

    /**
     * Add an item to this directory in the lexicographically correct place
     *
     * @post Given item is added to directory
     *      | new.hasAsItem(item)
     *
     * @param item Item to add to this directory
     *
     * @note This method is package-private!
     */
    void addItem(Item item) {
        // Don't just add it to the back, but insert it in the lexicographically correct place!
        int index = 0;
        while (index < getNbItems() && items.get(index).lexicographicallyBelongsBefore(item)) {
            index++;
        }
        items.add(index, item);
    }

    /**
     * Remove an item from this directory
     *
     * @post Given item is removed from directory
     *      | !new.hasAsItem(item)
     *
     * @param item Item to remove from this directory
     *
     * @note This method is package-private!
     */
    void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Get the number of items in this directory
     *
     * @return Number of items in this directory
     *      | items.size()
     */
    public int getNbItems() {
        return items.size();
    }

    /**
     * Get the item in this directory with given name
     *
     * @pre Given name is not null
     *      | name != null
     *
     * @param name Name of the item to retrieve
     *
     * @return Item that has given name or null if no such item is present
     *      | hoe de fuck moe ik dit formeel noteren bloedje
     */
    public Item getItem(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Check whether this directory contains an item with the given name
     *
     * @pre Given name is not null
     *      | name != null
     *
     * @param name Name to check for
     *
     * @return True if this directory contains an item with given name; false otherwise
     *      | getItem(name) != null
     */
    public boolean containsDiskItemWithName(String name) {
        return getItem(name) != null;
    }

    /**
     * Get the item at given ordered position in this directory
     *
     * @param pos Position in the directory
     *
     * @return Item at given ordered position in this directory; null if position out of bounds
     *      | hoe hoe hoe
     */
    public Item getItemAt(int pos) {
        if (pos < 1 || pos > getNbItems()) {
            return null;
        }
        return items.get(pos-1); // Minus one because user's pos value starts at 1, while indices for list start at 0
    }

    /**
     * Checks whether this directory contains given item
     *
     * @pre Given item is not null
     *      | item != null
     *
     * @param item Item to check appearance for
     *
     * @return True if this directory contains given item; false otherwise
     *      | items.contains(item)
     */
    public boolean hasAsItem(Item item) {
        return items.contains(item);
    }

    // =================================================================================
    // Other methods
    // =================================================================================

    /**
     * Finds the root directory this directory is directly or indirectly a child of
     *
     * @return The root directory this directory is directly or indirectly a child of
     *      | bloedje wtf dit kan ik nu toch echt ni formeel doen
     */
    // Overwritten version of Item's implementation, because a Directory could be a root itself and that detection happens here!
    @Override
    public Directory getRoot() {
        if (this.isRoot()) {
            return this;
        }

        // Not a root, just use the standard recursion (defined in superclass Item)
        return super.getRoot();
    }

    /**
     * Get the absolute file path for this directory
     *
     * @return String absolute file path for this directory
     *      | ????????
     */
    @Override
    public String getAbsolutePath() {
        if (isRoot()) {
            return "/"+getName();
        }

        return getParentDirectory().getAbsolutePath()+"/"+getName();
    }

    /**
     * Get the total disk usage for this directory
     *
     * @return Total disk usage for this directory
     *      | bro
     */
    @Override
    public int getTotalDiskUsage() {
        int total = 0;
        for (Item i : items) {
            total = total + i.getTotalDiskUsage();
        }
        return total;
    }
}