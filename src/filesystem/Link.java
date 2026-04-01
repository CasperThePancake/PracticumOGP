package filesystem;

/**
 * A basic link class for OGP Practicum
 *
 * @invar The linked item of the link must always be valid or the linked item must be terminated
 *      | canHaveAsLinkedItem(linkedItem) || linkedItem.isTerminated()
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Link extends Item {
    // =================================================================================
    // Attributes
    // =================================================================================
    private static final String defaultName = "New-Link";
    private Item linkedItem = null;

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create a link in given directory, with given name, linked to given item
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
     * @effect Link is moved to given directory
     *      | move(directory)
     *
     * @effect Linked item is set to given item
     *      | setLinkedItem(linkedItem)
     *
     * @param directory Given target directory
     * @param name Given name for the link
     * @param linkedItem Given item to link with
     */
    public Link(Directory directory, String name, Item linkedItem) {
        super(name, true, directory);
        this.setLinkedItem(linkedItem);
    }

    // =================================================================================
    // linkedItem
    // =================================================================================

    /**
     * Get the item this link is linked to
     *
     * @return Item this link is linked to
     *      | linkedItem
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    public Item getLinkedItem() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return linkedItem.isTerminated() ? null : linkedItem;
    }

    /**
     * Set the item this link is linked with
     *
     * @post Linked item is given item
     *      | new.getLinkedItem() == linkedItem
     *
     * @param linkedItem Item to link the link with
     *
     * @throws LinkException If given item cannot be linked to
     *      | linkedItem == null || !linkedItem.canBeLinkedTo()
     */
    private void setLinkedItem(Item linkedItem) throws LinkException {
        if (canHaveAsLinkedItem(linkedItem)) {
            this.linkedItem = linkedItem;
        } else {
            throw new LinkException("This item cannot be linked to!");
        }
    }

    /**
     * Check whether the given item can be linked to
     *
     * @param linkedItem Item to check
     *
     * @return True if the given item is not null and can be linked to internally; false otherwise
     *      | linkedItem != null && linkedItem.canBeLinkedTo()
     */
    public static boolean canHaveAsLinkedItem(Item linkedItem) {
        return linkedItem != null && linkedItem.canBeLinkedTo();
    }

    // =================================================================================
    // Other methods
    // =================================================================================

    /**
     * Get the absolute file path for this link
     *
     * @return The result is the absolute path of its
     * parent directory followed by a forward slash and the name of this link.
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

        return getParentDirectory().getAbsolutePath()+"/"+getName();
    }

    /**
     * Get the total disk usage for this link
     *
     * @return Total disk usage for this link
     *      | 0
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public int getTotalDiskUsage() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return 0;
    }

    /**
     * Delete and terminate this link
     *
     * @throws DeleteException If link cannot be deleted and terminated
     *      | !canBeDeleted()
     *
     * @post Link is terminated and deleted
     *      | isTerminated()
     */
    @Override
    public void delete() throws DeleteException {
        if (canBeDeleted()) {
            getParentDirectory().removeItem(this);
            setTerminated(true);
        } else {
            throw new DeleteException("This link cannot be deleted!");
        }
    }

    /**
     * Check whether this link can be deleted and terminated
     *
     * @return True if link is not yet terminated; false otherwise
     *      | !isTerminated()
     */
    public boolean canBeDeleted() {
        return !isTerminated();
    }

    /**
     * Check whether this link can be linked to
     *
     * @return False
     *      | false
     *
     * @throws IllegalStateException If item is terminated
     *      | isTerminated()
     */
    @Override
    public boolean canBeLinkedTo() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This item has been deleted!");
        }

        return false;
    }

    /**
     * Get the default name for this link
     *
     * @return Default name for this link
     *      | Link.defaultName
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
