package core.api.file.format.separator;

import java.io.File;

public class CSVFile extends SeparatorFile {

    public CSVFile(File file) {
        super(file);
    }

    public CSVFile(String file) {
        this(new File(file));
    }

    public CSVFile(File parent, String child) {
        this(new File(parent, child));
    }

    public CSVFile(String parent, String child) {
        this(new File(parent, child));
    }

    @Override
    public String getDelimiter() {
        return ",";
    }
}
