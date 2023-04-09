package core.api.sql;

import core.annotation.FieldsAreNullableByDefault;
import core.annotation.MethodsReturnNonnullByDefault;
import core.api.file.format.PropertiesFile;

import java.io.File;

@FieldsAreNullableByDefault
@MethodsReturnNonnullByDefault
public class Database {
    private static SQLConnection connection = null;

    public static synchronized SQLConnection getConnection() {
        File file = new File(new File("core", ".sql"), "config.properties");
        return connection == null ? (connection = SQL.connect(new PropertiesFile(file))) : connection;
    }

    public static synchronized void disconnect() {
        if (connection != null) connection.close();
    }

    public static boolean hasConnection() {
        return connection != null;
    }
}
