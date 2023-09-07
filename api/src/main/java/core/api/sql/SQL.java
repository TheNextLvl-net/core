package core.api.sql;

import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.api.file.format.PropertiesFile;
import core.api.file.format.TextFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
        List<String> content = password.isBlank() ? List.of() : new TextFile(config.getFile().getParent(), password) {{
            if (getRoot().isEmpty()) setRoot(List.of("your_password")).save();
        }}.getRoot();
        SQLConnection connection = new SQLConnection(url, username, content.isEmpty() ? null : content.get(0), driver);
        CONNECTIONS.add(connection);
        return connection;
    }

    public static void disconnect() {
        new ArrayList<>(CONNECTIONS).forEach(SQLConnection::close);
    }
}
