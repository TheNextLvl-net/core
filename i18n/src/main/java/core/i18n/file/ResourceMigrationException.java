package core.i18n.file;

public class ResourceMigrationException extends RuntimeException {
    /**
     * Constructs a new ResourceMigrationException with the specified detail message.
     * <p>
     * This exception is specifically used to indicate issues encountered during resource migration.
     *
     * @param message the detail message providing additional context about the exception.
     */
    public ResourceMigrationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceMigrationException with the specified detail message and cause.
     * <p>
     * This exception is specifically used to indicate issues encountered during resource migration.
     *
     * @param message the detail message providing additional context about the exception.
     * @param cause   the cause of the exception, which can be used to retrieve details about the root issue.
     */
    public ResourceMigrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
