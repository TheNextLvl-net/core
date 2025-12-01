import org.jspecify.annotations.NullMarked;

@NullMarked
module core.files {
    requires com.google.gson;

    requires static org.jspecify;

    exports core.file.formats;
    exports core.file;
}