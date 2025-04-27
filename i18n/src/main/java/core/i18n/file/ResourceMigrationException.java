package core.i18n.file;

public class ResourceMigrationException extends RuntimeException {
    public ResourceMigrationException(String message) {
        super(message);
    }

    public ResourceMigrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
