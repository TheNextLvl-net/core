package core.api.file.format.separator;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class CSVFile extends SeparatorFile {

    /**
     * Construct a new CSVFile providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public CSVFile(File file, Charset charset, List<List<String>> root) {
        super(file, charset, root);
    }

    /**
     * Construct a new CSVFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public CSVFile(File file, Charset charset) {
        super(file, charset);
    }

    /**
     * Construct a new CSVFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public CSVFile(File file, List<List<String>> root) {
        super(file, root);
    }

    /**
     * Construct a new CSVFile providing a file
     *
     * @param file the file to read from and write to
     */
    public CSVFile(File file) {
        super(file);
    }

    @Override
    public CSVFile save(File file) {
        return (CSVFile) super.save(file);
    }

    @Override
    public CSVFile save() {
        return (CSVFile) super.save();
    }

    @Override
    public CSVFile saveIfAbsent() {
        return (CSVFile) super.saveIfAbsent();
    }

    @Override
    public final String getDelimiter() {
        return ",";
    }
}
