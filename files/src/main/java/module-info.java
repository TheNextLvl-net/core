/**
 * @deprecated This module has been deprecated and is no longer maintained.
 */
@Deprecated
module core.files {
    requires com.google.gson;

    requires static org.jspecify;

    exports core.file;
    exports core.file.format;
    exports core.file.format.separator;
    exports core.io;
}