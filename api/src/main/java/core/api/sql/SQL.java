package core.api.sql;

import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.api.file.format.PropertiesFile;
import core.api.file.format.TextFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Deprecated(forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "4.1.0")
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQL {
    static final List<SQLConnection> CONNECTIONS = new ArrayList<>();

    public static SQLConnection connect(PropertiesFile config) {
        String url = config.getRoot().getString("url", "jdbc:mysql://localhost:3306/mysql?autoReconnect=true");
        String username = config.getRoot().getString("username", "root");
        String password = config.getRoot().getString("password", "password.txt");
        String driver = config.getRoot().getString("driver", "com.mysql.cj.jdbc.Driver");
        config.getRoot().set("url", url);
        config.getRoot().set("username", username);
        config.getRoot().set("password", password);
        config.getRoot().set("driver", driver);
        config.save();
        var file = new File(config.getFile().getParent(), password);
        var content = password.isBlank() ? List.<String>of() : new TextFile(file, List.of("your_password"))
                .saveIfAbsent().getRoot();
        var connection = new SQLConnection(url, username, content.isEmpty() ? null : content.get(0), driver);
        CONNECTIONS.add(connection);
        return connection;
    }

    public static void disconnect() {
        new ArrayList<>(CONNECTIONS).forEach(SQLConnection::close);
    }
}
