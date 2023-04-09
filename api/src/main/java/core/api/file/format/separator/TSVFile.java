package core.api.file.format.separator;

import java.io.File;

public class TSVFile extends SeparatorFile {

    public TSVFile(File file) {
        super(file);
    }

    public TSVFile(String file) {
        this(new File(file));
    }

    public TSVFile(File parent, String child) {
        this(new File(parent, child));
    }

    public TSVFile(String parent, String child) {
        this(new File(parent, child));
    }

    @Override
    public String getDelimiter() {
        return "\t";
    }
}
