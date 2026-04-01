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
    static final Directory NULL_ROOT = new NullDirectory(); // Package-private
    private static final String defaultName = "New-Directory";

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
        super(cleanName(name), writable, parentDirectory);
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
        super(cleanName(name), true, parentDirectory);
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
        super(cleanName(name), writable, NULL_ROOT);
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
        super(cleanName(name), true, NULL_ROOT);
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
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public void makeRoot() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        this.move(NULL_ROOT);
    }

    /**
     * Check whether this directory is a root directory
     *
     * @return True if this directory is a root directory; false otherwise
     *      | getParentDirectory() == null
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public boolean isRoot() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

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
     * @throws WriteException If directory is not writable
     *      | !isWritable()
     *
     * @note This method is package-private!
     *
     * @note No checks are being run for the validity of the item argument, since this method is always run from a valid item itself!
     */
    void addItem(Item item) throws WriteException {
        if (!isWritable()) {
            throw new WriteException("This directory is read-only!");
        }

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
     * @throws WriteException If directory is not writable
     *      | !isWritable()
     *
     * @note This method is package-private!
     *
     * @note No checks are being run for the validity of the item argument, since this method is always run from a valid item itself!
     */
    void removeItem(Item item) throws WriteException {
        if (!isWritable()) {
            throw new WriteException("This directory is read-only!");
        }

        items.remove(item);
    }

    /**
     * Get the number of items in this directory
     *
     * @return Number of items in this directory
     *      | items.size()
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public int getNbItems() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

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
     *      | if (exists i in items: i.getName().equalsIgnoreCase(name))
     *      |   then (result != null && result.getName().equalsIgnoreCase(name) && items.contains(result))
     *      | else (result == null)
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public Item getItem(String name) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

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
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public boolean containsDiskItemWithName(String name) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return getItem(name) != null;
    }

    /**
     * Get the item at given ordered position in this directory
     *
     * @param pos Position in the directory (starting at 1)
     *
     * @return Item at given ordered position in this directory; null if position out of bounds
     *      | if (pos >= 1 && pos <= getNbItems())
     *      |    then (result == items.get(pos - 1))
     *      | else (result == null)
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public Item getItemAt(int pos) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

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
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public boolean hasAsItem(Item item) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return items.contains(item);
    }

    // =================================================================================
    // Other methods
    // =================================================================================

    /**
     * Static method to further filter names specifically for directories
     *
     * @param name Given name to filter
     *
     * @return Filtered name
     *      | result.equals(name.replaceAll("[.]", ""))
     */
    public static String cleanName(String name) {
        return name.replaceAll("[.]", "");
    }

    /**
     * Finds the root directory this directory is directly or indirectly a child of
     *
     * @return The root directory this directory is directly or indirectly a child of
     *      | this.isDirectOrIndirectChildOf(result) && result.isRoot()
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    // Overwritten version of Item's implementation, because a Directory could be a root itself and that detection happens here!
    @Override
    public Directory getRoot() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        if (this.isRoot()) {
            return this;
        }

        // Not a root, just use the standard recursion (defined in superclass Item)
        return super.getRoot();
    }

    /**
     * Get the absolute file path for this directory
     *
     * @return If this directory is a root, the result is its name preceded by a forward slash.
     *      | if (this.isRoot())
     *      |    then (result.equals("/" + getName()))
     * @return If this directory is not a root, the result is the absolute path of its
     * parent directory followed by a forward slash and the name of this directory.
     *      | if (!this.isRoot())
     *      |    then (result.equals(getParentDirectory().getAbsolutePath() + "/" + getName()))
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public String getAbsolutePath() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        if (isRoot()) {
            return "/"+getName();
        }

        return getParentDirectory().getAbsolutePath()+"/"+getName();
    }

    /**
     * Get the total disk usage for this directory
     *
     * @return Total disk usage for this directory
     *      | result == sum(getTotalDiskUsage(i) for i if this.hasAsItem(i))
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public int getTotalDiskUsage() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        int total = 0;
        for (Item i : items) {
            total = total + i.getTotalDiskUsage();
        }
        return total;
    }

    /**
     * Delete and terminate this directory
     *
     * @throws DeleteException If directory cannot be deleted
     *      | !canBeDeleted()
     *
     * @post Directory is terminated
     *      | isTerminated()
     */
    @Override
    public void delete() throws DeleteException {
        if (canBeDeleted()) {
            getParentDirectory().removeItem(this);
            setTerminated(true);
        } else {
            throw new DeleteException("This directory cannot be deleted!");
        }
    }

    /**
     * Check whether this directory can be deleted and terminated
     *
     * @return True if directory is writable, empty, and not yet terminated; false otherwise
     *      | isWritable() && getNbItems() == 0 && !isTerminated()
     */
    public boolean canBeDeleted() {
        return isWritable() && getNbItems() == 0 && !isTerminated();
    }

    /**
     * Check whether this directory can be linked to
     *
     * @return True
     *      | true
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public boolean canBeLinkedTo() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return true;
    }

    /**
     * Get the default name for this directory
     *
     * @return Default name for this directory
     *      | Directory.defaultName
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public String getDefaultName() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return defaultName;
    }
}