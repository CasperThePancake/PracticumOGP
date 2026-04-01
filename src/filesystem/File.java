package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A basic file class for OGP Practicum
 *
 * @invar The size of the file must always be valid
 *      | canHaveAsSize(getSize())
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class File extends Item {
    // =================================================================================
    // Attributes
    // =================================================================================
    private int size = 0;
    private static final int maxSize = Integer.MAX_VALUE;
    private final FileExtension fileExtension;
    private static final String defaultName = "New-File";

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create an empty, writable file with given name
     *
     * @effect Name is set to given name
     *      | setName(name)
     *
     * @effect Size is set to zero
     *      | setSize(0)
     *
     * @effect Writability is set to true
     *      | setWritable(true)
     *
     * @effect File is moved to given directory
     *      | move(parentDirectory)
     *
     * @param name The name for the file
     * @param fileExtension The file extension of the file
     */
    @Raw
    public File(Directory parentDirectory, String name, FileExtension fileExtension) {
        this(parentDirectory,name,0,true,fileExtension);
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
     * @effect File is moved to given directory
     *      | move(parentDirectory)
     *
     * @param parentDirectory Given directory for the file
     * @param name The name for the file
     * @param size The size for the file
     * @param writable The writability for the file
     * @param fileExtension The file extension of the file
     */
    @Raw
    public File(Directory parentDirectory, String name, int size, boolean writable, FileExtension fileExtension) {
        super(name,writable,parentDirectory);
        this.setSize(size);
        this.fileExtension = fileExtension;
    }

    // =================================================================================
    // FileExtension
    // =================================================================================

    /**
     * Get the file extension of the file
     *
     * @return File extension of the file
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Raw @Basic
    public FileExtension getFileExtension() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return fileExtension;
    }

    // =================================================================================
    // Size
    // =================================================================================

    /**
     * Returns whether the given size is a valid size
     *
     * @param size Size to check
     *
     * @return True if the size is positive and less than or equal to the maxSize
     *      | size >= 0 && size <= maxSize
     */
    public static boolean canHaveAsSize(int size) {
        return size >= 0 && size <= maxSize;
    }

    /**
     * Set the size for a file
     *
     * @pre The given size is a valid size
     *      | canHaveAsSize(size)
     *
     * @post Size is given size
     *      | new.getSize() == size
     *
     * @param size The given size for the file
     */
    @Model
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * Change the size for a file
     *
     * @effect Size is given size
     *      | setSize(size)
     *
     * @post Modify time is set to current time
     *      | new.getModifyTime() == new Date()
     *
     * @param size The given size for the file
     *
     * @throws WriteException If file is not writable
     *      | !isWritable()
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public void changeSize(int size) throws WriteException, IllegalStateException {
        if (!this.isWritable()) {
            throw new WriteException("File is not writable");
        }

        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        this.size = size;
        this.setModifyTime();
    }

    /**
     * Increases file size by given amount
     *
     * @pre The given amount is strictly positive
     *      | amount > 0
     *
     * @effect Size is size plus given amount
     *      | changeSize(getSize() + amount)
     *
     * @param amount Amount to enlarge size by
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public void enlarge(int amount) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        this.changeSize(this.size + amount);
    }

    /**
     * Decreases file size by given amount
     *
     * @pre The given amount is strictly positive
     *      | amount > 0
     *
     * @effect Size is size plus given amount
     *      | changeSize(getSize() - amount)
     *
     * @param amount Amount to shorten size by
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public void shorten(int amount) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        this.changeSize(this.size - amount);
    }

    /**
     * Get the size of the file
     *
     * @return Size of the file
     *      | size
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Basic @Raw
    public int getSize() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return size;
    }

    // =================================================================================
    // Other methods
    // =================================================================================

    /**
     * Get the absolute file path for this file
     *
     * @return The result is the absolute path of its
     * parent directory followed by a forward slash and the name of this file.
     *      | result.equals(getParentDirectory().getAbsolutePath() + "/" + getName())
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public String getAbsolutePath() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return getParentDirectory().getAbsolutePath()+"/"+getName()+"."+getFileExtension().getSuffix();
    }

    /**
     * Get the total disk usage for this file
     *
     * @return Total disk usage for this file
     *      | this.getSize()
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public int getTotalDiskUsage() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return getSize();
    }

    /**
     * Check whether this file can be linked to
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
     * Delete and terminate this file
     *
     * @throws DeleteException If file cannot be deleted and terminated
     *      | !canBeDeleted()
     *
     * @post File is terminated and deleted
     *      | isTerminated() && !getParentDirectory.hasAsItem(this)
     */
    @Override
    public void delete() throws DeleteException {
        if (canBeDeleted()) {
            getParentDirectory().removeItem(this);
            setTerminated(true);
        } else {
            throw new DeleteException("This file cannot be deleted!");
        }
    }

    /**
     * Check whether this file can be deleted and terminated
     *
     * @return True if file is writable and not yet terminated; false otherwise
     *      | isWritable() && !isTerminated()
     */
    public boolean canBeDeleted() {
        return isWritable() && !isTerminated();
    }

    /**
     * Get the default name for this file
     *
     * @return Default name for this file
     *      | File.defaultName
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
