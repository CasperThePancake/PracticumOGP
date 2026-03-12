package filesystem;

/**
 * File extension enum
 */
public enum FileExtension {
    // Enum constants (extendable)
    TXT("txt"),
    PDF("pdf"),
    JAVA("java");

    // Store the suffix (file extension string)
    private final String suffix;

    // Constructor
    private FileExtension(String suffix) {
        this.suffix = suffix;
    }

    // Getter method for suffix
    public String getSuffix() {
        return suffix;
    }
}