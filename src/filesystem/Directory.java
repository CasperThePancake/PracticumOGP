package filesystem;

import java.util.ArrayList;
import java.util.List;

public class Directory extends Item {
    // =================================================================================
    // Attributes
    // =================================================================================
    private List<Item> items = new ArrayList<>();
    static final Directory NULL_ROOT = new NullDirectory();

    // =================================================================================
    // Constructors
    // =================================================================================

    // This one only used for NullDirectory! (package-private)
    Directory(boolean internal) {
        super(internal);
    }

    public Directory(Directory directory, String name, boolean writable) {
        super(name, writable, directory);
    }

    public Directory(Directory directory, String name) {
        super(name, true, directory);
    }

    public Directory(String name, boolean writable) {
        super(name, writable, NULL_ROOT);
    }

    public Directory(String name) {
        super(name, true, NULL_ROOT);
    }

    // =================================================================================
    // Root directories
    // =================================================================================

    public void makeRoot() {
        this.move(NULL_ROOT);
    }

    public boolean isRoot() {
        return this.getParentDirectory() == NULL_ROOT;
    }

    // No removeRoot(), you can just put the folder into a real folder to make it no longer root

    // =================================================================================
    // Items
    // =================================================================================

    // Add and remove here are package-private: the user cannot run them, other classes CAN, because this can only be called by Item's move() method
    // In other words: Item keeps track of the bidirectional association

    void addItem(Item item) {
        // Don't just add it to the back, but insert it in the lexicographically correct place!
        int index = 0;
        while (index < getNbItems() && items.get(index).lexicographicallyBelongsBefore(item)) {
            index++;
        }
        items.add(index, item);
    }

    void removeItem(Item item) {
        items.remove(item);
    }

    public int getNbItems() {
        return items.size();
    }

    public Item getItem(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return null;
    }

    public Item getItemAt(int pos) {
        return items.get(pos-1); // Minus one because user's pos value starts at 1, while indices for list start at 0
    }

    public boolean hasAsItem(Item item) {
        return items.contains(item);
    }
}