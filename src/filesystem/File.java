package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Date;

/**
 * A basic file class for OGP Practicum 1
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class File extends Item {

    private int size;
    private static final int maxSize = Integer.MAX_VALUE;
    private final FileExtension fileExtension;

    // Constructors

    /**
     * Create an empty, writable file with given name
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @param name The name for the file
     */
    public File(String name, FileExtension fileExtension) {
        super(name,true);
        this.setSize(0,true);
        this.fileExtension = fileExtension;
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
    public File(String name, int size, boolean writable, FileExtension fileExtension) {
        super(name,writable);
        this.setSize(size,true);
        this.fileExtension = fileExtension;
    }

    // Other methods

    /**
     * Get the file extension of the file
     *
     * @return File extension of the file
     */
    public FileExtension getFileExtension() {
        return fileExtension;
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
            this.setModifyTime(new Date());
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
     * Get the size of the file
     *
     * @return Size of the file
     */
    @Basic @Raw
    public int getSize() {
        return size;
    }
}
