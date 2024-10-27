package core.file.format.separator;

import core.io.IO;
import org.jspecify.annotations.NullMarked;

import java.nio.charset.Charset;
import java.util.List;

/**
 * A class for handling CSV (Comma-Separated Values) files.
 * Extends {@link SeparatorFile} to provide methods specific to managing CSV files.
 */
@NullMarked
public class CSVFile extends SeparatorFile {
    /**
     * Construct a new CSVFile providing a file, charset and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public CSVFile(IO io, Charset charset, List<List<String>> root) {
        super(io, charset, root);
    }

    /**
     * Construct a new CSVFile providing a file and charset
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public CSVFile(IO io, Charset charset) {
        super(io, charset);
    }

    /**
     * Construct a new CSVFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public CSVFile(IO io, List<List<String>> root) {
        super(io, root);
    }

    /**
     * Construct a new CSVFile providing a file
     *
     * @param io the file to read from and write to
     */
    public CSVFile(IO io) {
        super(io);
    }

    @Override
    public final String getDelimiter() {
        return ",";
    }
}
