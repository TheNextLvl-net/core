package core.file.format.separator;

import core.io.IO;
import org.jspecify.annotations.NullMarked;

import java.nio.charset.Charset;
import java.util.List;

/**
 * A class representing a Tab-Separated Values (TSV) file. This class extends {@link SeparatorFile}
 * and provides functionality to read from and write to TSV files.
 */
@NullMarked
public class TSVFile extends SeparatorFile {

    /**
     * Construct a new TSVFile providing a file, charset and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public TSVFile(IO io, Charset charset, List<List<String>> root) {
        super(io, charset, root);
    }

    /**
     * Construct a new TSVFile providing a file and charset
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public TSVFile(IO io, Charset charset) {
        super(io, charset);
    }

    /**
     * Construct a new TSVFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public TSVFile(IO io, List<List<String>> root) {
        super(io, root);
    }

    /**
     * Construct a new TSVFile providing a file
     *
     * @param io the file to read from and write to
     */
    public TSVFile(IO io) {
        super(io);
    }

    @Override
    public final String getDelimiter() {
        return "\t";
    }
}
