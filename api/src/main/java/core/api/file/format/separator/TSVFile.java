package core.api.file.format.separator;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class TSVFile extends SeparatorFile {

    /**
     * Construct a new TSVFile providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public TSVFile(File file, Charset charset, List<List<String>> root) {
        super(file, charset, root);
    }

    /**
     * Construct a new TSVFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public TSVFile(File file, Charset charset) {
        super(file, charset);
    }

    /**
     * Construct a new TSVFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public TSVFile(File file, List<List<String>> root) {
        super(file, root);
    }

    /**
     * Construct a new TSVFile providing a file
     *
     * @param file the file to read from and write to
     */
    public TSVFile(File file) {
        super(file);
    }

    @Override
    public final String getDelimiter() {
        return "\t";
    }

    @Override
    public TSVFile save() {
        return (TSVFile) super.save();
    }

    @Override
    public TSVFile saveIfAbsent() {
        return (TSVFile) super.saveIfAbsent();
    }
}
