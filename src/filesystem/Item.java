package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Date;

/**
 * Abstract item class to serve as foundation for File, Directory, and Link
 *
 * @author Casper Vermeeren; Loïck Sansen
 *
 * @invar The creation time must always be valid
 *       | isValidCreationTime(getCreationTime())
 *
 * @invar The modification time must always be valid
 *       | canHaveAsModificationTime(getModificationTime())
 *
 * @invar The name of the item must always be valid
 *      | isValidName(getName())
 *
 * @invar The parentDirectory of the item must always be valid
 *      | canHaveAsParentDirectory(getParentDirectory())
 */
public abstract class Item {
    // =================================================================================
    // Attributes
    // =================================================================================
    private String name = "New-Item";
    private Date createTime = null;
    private Date modifyTime = null;
    private boolean writable = true;
    private Directory parentDirectory = null;

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create an item with given name and writability
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @effect Writability is set to given writability
     *      | setWritable(writable)
     *
     * @effect Create time is set to current time
     *      | setCreateTime()
     *
     * @param name Given name for the item
     *
     * @param writable Given writability for the item
     */
    public Item(String name, boolean writable, Directory directory) {
        this.setName(name);
        this.setWritable(writable);
        this.setCreateTime();
        this.modifyTime = null;
        this.move(directory);
    }

    /**
     * Create an internal item placeholder
     *
     * @param internal Whether this item is for internal use only
     *
     * @note Used for creating NULL_ROOT placeholder as parentDirectory for root directories
     * @note This method is package-private to prevent users from using it
     */
    Item(boolean internal) {
        this.parentDirectory = null;
    }

    // =================================================================================
    // Parent directory
    // =================================================================================

    /**
     * Get the parent directory for this item
     *
     * @return Null if item is root, parent directory otherwise
     *      | hoe de fuck formeel
     *
     * @note We are careful here to not return the NULL_ROOT object to the user, as letting them have access to it could break everything
     */
    @Raw
    public Directory getParentDirectory() {
        if (parentDirectory == Directory.NULL_ROOT) {
            return null;
        }
        return parentDirectory;
    }

    /**
     * Set the parent directory for this item
     *
     * @param parentDirectory The directory to set as parent for this item
     */
    private void setParentDirectory(Directory parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    /**
     * Returns whether given directory can be a parent directory of this item
     *
     * @param parentDirectory Parent directory to check
     *
     * @return True if given directory is not null and given directory is not this item and given directory does not contain something with this item's name and given directory is not child of this item; false otherwise
     *      | parentDirectory != null && parentDirectory != this && parentDirectory.getItem(this.name()) == null && !parentDirectory.isDirectOrIndirectChildOf(this)
     */
    public boolean canHaveAsParentDirectory(Directory parentDirectory) {
        // Valid parentDirectory?
        if (parentDirectory == null) {
            return false;
        }

        // Cannot put something in itself
        if (this == parentDirectory) {
            return false;
        }

        // parentdirectory cannot already contain something with the same name
        if (parentDirectory.containsDiskItemWithName(this.getName())) {
            return false;
        }

        // parentDirectory cannot be one of your own children (only meant for directories, but will just be false if this=file/link)
        if (parentDirectory.isDirectOrIndirectChildOf(this)) {
            return false;
        }

        return true;
    }

    /**
     * Checks recursively whether this item is a direct or indirect child of the given potentialParent item
     *
     * @param potentialParent Given potential parent item
     *
     * @return True if given potentialParent is this item or parent directory of this item is direct or indirect child of given potentialParent
     *      | this == potentialParent || this.getParentDirectory().isDirectOrIndirectChildOf(potentialParent)
     */
    public boolean isDirectOrIndirectChildOf(Item potentialParent) {
        if (this == potentialParent) {
            return true;
        }

        if (this == Directory.NULL_ROOT) {
            return false;
        }

        return parentDirectory.isDirectOrIndirectChildOf(potentialParent); // Don't use getter here because that one hides NULL_ROOT
    }

    /**
     * Move this item to the given targetDirectory
     *
     * @throws MoveException If this item cannot belong to the given targetDirectory
     *      | !canHaveAsParentDirectory(targetDirectory)
     *
     * @post If this item was already in a directory, remove this item from that directory
     *      | !this.getParentDirectory().hasAsItem(new)
     *
     * @post This item is added to given targetDirectory
     *      | targetDirectory().hasAsItem(new)
     *
     * @post This item's parentDirectory becomes targetDirectory
     *      | new.getParentDirectory() == targetDirectory
     *
     * @param targetDirectory Target directory to move this item to
     */
    public void move(Directory targetDirectory) throws MoveException {
        if (canHaveAsParentDirectory(targetDirectory)) {
            // Already in a folder? Remove me from that folder!
            if (this.getParentDirectory() != null) {
                this.getParentDirectory().removeItem(this);
            }
            this.setParentDirectory(targetDirectory);
            targetDirectory.addItem(this);
        } else {
            throw new MoveException("Item is unable to be moved into that directory!");
        }
    }

    /**
     * Finds the root directory this item is directly or indirectly a child of
     *
     * @return The root directory this item is directly or indirectly a child of
     *      | bloedje wtf dit kan ik nu toch echt ni formeel doen
     */
    public Directory getRoot() {
        return getParentDirectory().getRoot(); // For any non-directory item, you'll have to recurse up (this method is overwritten for directory!)
    }

    public abstract String getAbsolutePath();

    // =================================================================================
    // Name
    // =================================================================================

    /**
     * Checks if given name is valid
     *
     * @param name Name to check
     *
     * @return True if the given name only contains legal characters
     *      | name.matches(regexFilter)
     */
    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z0-9_.\\-]*");
    }

    /**
     * Removes any illegal characters from given name and returns it
     *
     * @param name Name to clean
     *
     * @return Given name with any illegal characters removed
     */
    private static String cleanIllegalName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_.\\-]", "");
    }

    /**
     * Set the name for an item
     *
     * @post If given name is valid and is not empty, name is given name
     *      | new.getName() == name
     * @post If given name is invalid and is not empty, name is filtered name
     *      | new.getName() == cleanIllegalName(name)
     * @post If given name or filtered name is empty, name is "New-Item"
     *      | new.getName() == "New-Item"
     *
     * @throws WriteException If item is not writable
     *      | !isWritable()
     *
     * @param name The given name for the item
     */
    @Model @Raw
    private void setName(String name) {
            if (isValidName(name)) {
                if (!name.isEmpty()) {
                    this.name = name;
                } else {
                    this.name = "New-item";
                }
            } else {
                String cleaned = cleanIllegalName(name);
                if (!cleaned.isEmpty()) {
                    this.name = cleaned;
                } else {
                    this.name = "New-item";
                }
            }
    }

    /**
     * Change the name for an item
     *
     * @effect Name is given name
     *      | setName(name)
     *
     * @param name The given name for the item
     *
     * @throws WriteException If item is not writable
     *      | !isWritable()
     */
    public void changeName(String name) throws WriteException {
        if (this.isWritable()) {
            // Remove and re-add this item after name is changed so that it is put in the right lexicographical place again (ordered directory)
            Directory currentParent = this.getParentDirectory();
            currentParent.removeItem(this);
            this.setName(name);
            currentParent.addItem(this);
            this.setModifyTime();
        } else {
            throw new WriteException("This file is read-only!");
        }
    }

    /**
     * Get the name of the item
     *
     * @return Name of the item
     */
    @Basic @Raw
    public String getName() {
        return name;
    }

    // =================================================================================
    // Writable
    // =================================================================================
    /**
     * Set the writability of an item
     *
     * @post Writability is given writability
     *      | new.isWritable() == writable
     *
     * @param writable Writability of the item
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    /**
     * Get the writability of the item
     *
     * @return Writability of the item
     */
    @Basic @Raw
    public boolean isWritable() {
        return writable;
    }

    // =================================================================================
    // CreateTime
    // =================================================================================
    /**
     * Set the creation time of the item
     *
     * @post Creation time is current time
     *      | new.getCreateTime() == new Date()
     */
    public void setCreateTime() { // TOTAAL PROGRAMMEREN
        this.createTime = new Date();
    }

    /**
     * Get the creation time of the item
     *
     * @return Creation time of the item
     */
    @Basic @Raw
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Checks if given creation time is valid
     *
     * @param creationTime Creation time to check
     *
     * @return True if the given creation time is not null
     *      | creationTime != null
     */
    public static boolean isValidCreationTime(Date creationTime) {
        return creationTime != null;
    }

    // =================================================================================
    // ModifyTime
    // =================================================================================

    /**
     * Set the modify time of the item
     *
     * @post Modify time is current time
     *      | new.getModifyTime() == new Date()
     */
    public void setModifyTime() {
        this.modifyTime = new Date();
    }

    /**
     * Get the modify time of the item
     *
     * @return Modify time of the item
     */
    @Basic @Raw
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * Check if given modify time is valid
     *
     * @param modifyTime Modify time to check
     *
     * @return True if the given modify time is null or after the creation time of the item
     *      | modifyTime == null || modifyTime.after(getCreateTime())
     */
    public boolean isValidModifyTime(Date modifyTime) {
        return modifyTime == null || modifyTime.after(this.getCreateTime());
    }

    // =================================================================================
    // Other methods
    // =================================================================================

    /**
     * Determines whether this item and another item have an overlapping use period
     *
     * @return False if the given other item is invalid; False if either one of the items has not been modified yet; False if use periods don't overlap; True otherwise
     *      | other != null && this.getModifyTime() != null && other.getModifyTime() != null && other.getCreateTime().before(this.getModifyTime()) && other.getModifyTime().after(this.getCreateTime())
     *
     * @param other The other item to compare with
     */
    public boolean hasOverlappingUsePeriod(Item other) {
        if (other == null) {
            return false;
        }
        // If one of the items hasn't been used yet, always return false
        if (this.getModifyTime() == null || other.getModifyTime() == null) {
            return false;
        }
        // Check overlap
        return other.getCreateTime().before(this.getModifyTime()) && other.getModifyTime().after(this.getCreateTime());
    }

    // Check if this item belongs before other item in an ordered list (like for use in directory's alphabetical sort)

    /**
     * Check if this item lexicographically belongs before given other item in an ordered directory
     *
     * @pre Given other item is not null
     *      | other != null
     *
     * @param other Other item to compare with
     *
     * @return True if this item lexicographically belongs before given other item; false otherwise
     *      | getName().compareToIgnoreCase(other.getName()) < 0
     */
    public boolean lexicographicallyBelongsBefore(Item other) {
        return this.getName().compareToIgnoreCase(other.getName()) < 0;
    }

    public abstract int getTotalDiskUsage();
}
