package filesystem;/*
DISCUSSION BOARD

__TODO implementatie__
- We moeten de nodige JUnit tests maken! (filesystem.FileTest.java)
 */

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Date;

/**
 * A basic file class for OGP Practicum 1
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class File {

    private int size;
    private String name;
    private Date createTime;
    private Date modifyTime;
    private boolean writable;
    private static final int maxSize = Integer.MAX_VALUE;

    // Constructors

    /**
     * Create an empty, writable file with given name
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @param name The name for the file
     */
    public File(String name) {
        this.setName(name,true);
        this.createTime = new Date();
        this.modifyTime = null;
        this.writable = true;
        this.setSize(0,true);
    }

    /**
     * Create a file with given name, size and writability
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @effect Size is set to given size
     *      | setSize(size)
     *
     * @effect Writability is set to given writability
     *      | setWritable(writable)
     *
     * @param name The name for the file
     * @param size The size for the file
     * @param writable The writability for the file
     */
    public File(String name, int size, boolean writable) {
        this.setName(name,true);
        this.createTime = new Date();
        this.modifyTime = new Date();
        this.setWritable(writable);
        this.setSize(size,true);
    }

    // Other methods
    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z0-9_.\\-]*");
    }

    private static String cleanIllegalName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_.\\-]", "");
    }

    /**
     * Set the name for a file
     *
     * @post If given name only contains letters, numbers, dots, dashes, and underscores and is not empty, name is given name
     * @post If given name contains illegal characters, name is given name filtered from these illegal characters
     * @post If given name or filtered given name is empty, name is 'New-filesystem.File'
     *
     * @throws WriteException If file is not writable
     *      | !isWritable()
     *
     * @param name The given name of the file
     */
    public void setName(String name) throws WriteException { // TOTAAL PROGRAMMEREN
        // Check if file writable
        if (this.isWritable()) {
            // Using regex to check if all characters in string are legal
            if (isValidName(name)) {
                if (!name.isEmpty()) {
                    this.name = name;
                } else {
                    this.name = "New-filesystem.File";
                }
            } else {
                String cleaned = cleanIllegalName(name);
                if (!cleaned.isEmpty()) {
                    this.name = cleaned;
                } else {
                    this.name = "New-filesystem.File";
                }
            }
            // In any case the file name will have been changed, so update modified time
            this.modifyTime = new Date();
        } else {
            throw new WriteException("This file is not writable!");
        }
    }

    // Private method to set the name while ignoring writability rules for use in constructors
    private void setName(String name, boolean ignoreWritability) {
        if (ignoreWritability) {
            // Using regex to check if all characters in string are legal
            if (isValidName(name)) {
                if (!name.isEmpty()) {
                    this.name = name;
                } else {
                    this.name = "New-filesystem.File";
                }
            } else {
                String cleaned = cleanIllegalName(name);
                if (!cleaned.isEmpty()) {
                    this.name = cleaned;
                } else {
                    this.name = "New-filesystem.File";
                }
            }
        } else {
            setName(name);
        }
    }

    /**
     * Returns whether the given size is a valid size
     *
     * @param size Size to check
     *
     * @return True if the size is positive and less than or equal to the maxSize
     *      | size >= 0 && size <= maxSize
     */
    public boolean canAcceptForSize(int size) {
        return size >= 0 && size <= maxSize;
    }

    /**
     * Set the size for a file
     *
     * @pre The given size is a valid size
     *      | canAcceptForSize(size)
     *
     * @post Size is given size
     *      | new.getSize() == size
     *
     * @throws WriteException If file is not writable
     *      | !isWritable()
     *
     * @param size The given size of the file
     */
    public void setSize(int size) throws WriteException { // NOMINAAL PROGRAMMEREN
        if (this.isWritable()) {
            this.size = size;

            // Update modified time
            this.modifyTime = new Date();
        } else {
            throw new WriteException("This file is not writable!");
        }
    }

    // Private method to set the size while ignoring writability rules for use in constructors
    private void setSize(int size, boolean ignoreWritability) {
        if (ignoreWritability) {
            this.size = size;
        }
    }

    /**
     * Set the writability of a file
     *
     * @post Writability is given writability
     *      | new.isWritable() == writable
     *
     * @param writable Writability of the file
     */
    public void setWritable(boolean writable) { // DEFENSIEF PROGRAMMEREN
        this.writable = writable;
    }

    /**
     * Increases file size by given amount
     *
     * @pre The given amount is strictly positive
     *      | amount > 0
     * @pre The current size plus given amount is a valid size
     *      | canHaveAsSize(this.size+amount)
     *
     * @post Size is size plus given amount
     *      | new.getSize() == getSize() + amount
     *
     * @param amount Amount to enlarge size by
     */
    public void enlarge(int amount) { // NOMINAAL PROGRAMMEREN
        this.setSize(this.size + amount);
    }

    /**
     * Decreases file size by given amount
     *
     * @pre The given amount is strictly positive
     *      | amount > 0
     *
     * @pre The current size minus the given amount is a valid size
     *      | canHaveAsSize(this.size-amount)
     *
     * @post Size is size minus given amount
     *      | new.getSize() == getSize() - amount
     *
     * @param amount Amount to shorten size by
     */
    public void shorten(int amount) { // NOMINAAL PROGRAMMEREN
        this.setSize(this.size - amount);
    }

    /**
     * Set the creation time of the file
     *
     * @post If given date is invalid, set createTime to new date object
     *
     * @post createTime is given date otherwise
     *
     * @param createTime Creation date for the file
     */
    public void setCreateTime(Date createTime) { // TOTAAL PROGRAMMEREN
        if (createTime != null) {
            this.createTime = createTime;
        } else {
            this.createTime = new Date();
        }
    }

    /**
     * Set the modify time of the file
     *
     * @post If given date is invalid, set modifyTime to new date object
     *
     * @post modifyTime is given date otherwise
     *
     * @param modifyTime Modify date for the file
     */
    public void setModifyTime(Date modifyTime) { // TOTAAL PROGRAMMEREN
        if (modifyTime != null) {
            this.modifyTime = modifyTime;
        } else {
            this.modifyTime = new Date();
        }
    }

    /**
     * Get the size of the file
     *
     * @return Size of the file
     */
    @Basic @Raw
    public int getSize() {
        return size;
    }

    /**
     * Get the name of the file
     *
     * @return Name of the file
     */
    @Basic @Raw
    public String getName() {
        return name;
    }

    /**
     * Get the creation time of the file
     *
     * @return Creation time of the file
     */
    @Basic @Raw
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Get the last modified time of the file
     *
     * @return Last modified time of the file
     */
    @Basic @Raw
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * Get the writability of the file
     *
     * @return Writability of the file
     */
    @Basic @Raw
    public boolean isWritable() {
        return writable;
    }

    /**
     * Determines whether this file and another file have an overlapping use period
     *
     * @return False if the specified other file is invalid; False if either one of the files has not been modified yet after creation; True if modifyDate of oldest file is after creationDate of newest file; False otherwise
     *
     * @param other The other file to compare with
     */
    public boolean hasOverlappingUsePeriod(File other) { // TOTAAL PROGRAMMEREN
        if (other == null) {
            return false;
        }
        // If one of the files hasn't been used yet, always return false
        if (this.getModifyTime() == null || other.getModifyTime() == null) {
            return false;
        }
        // First, determine which file is the oldest
        if (this.getCreateTime().after(other.getCreateTime())) { // Other is oldest
            return other.getModifyTime().after(this.getCreateTime());
        } else { // This is oldest
            return this.getModifyTime().after(other.getCreateTime());
        }
    }
}
