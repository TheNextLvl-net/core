package core.sql;

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQLConnection {
    private final String url, username;
    private final @Nullable String password;

    public synchronized ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        try (var connection = getConnection(); var statement = connection.prepareStatement(query)) {
            for (var i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            var resultCached = RowSetProvider.newFactory().createCachedRowSet();
            resultCached.populate(statement.executeQuery());
            return resultCached;
        }
    }

    public synchronized void executeUpdate(String query, Object... parameters) throws SQLException {
        try (var connection = getConnection(); var statement = connection.prepareStatement(query)) {
            for (var i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            statement.executeUpdate();
        }
    }

    private @NotNull Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder {
        private String url = "jdbc:mysql://localhost:3306/mysql?autoReconnect=true";
        private String username = "root";
        private String driver = "com.mysql.cj.jdbc.Driver";
        private @Nullable String password;

        @SneakyThrows(ClassNotFoundException.class)
        public SQLConnection build() {
            Class.forName(this.driver);
            return new SQLConnection(url, username, password);
        }
    }
}
