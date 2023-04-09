package core.api.sql;

import core.annotation.FieldsAreNonnullByDefault;
import core.annotation.MethodsReturnNullableByDefault;
import core.annotation.ParametersAreNonnullByDefault;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.io.Closeable;
import java.sql.*;

@RequiredArgsConstructor
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNullableByDefault
public class SQLConnection implements Closeable {
    private final String url, username, driver;
    private final @Nullable String password;

    public synchronized ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            CachedRowSet resultCached = RowSetProvider.newFactory().createCachedRowSet();
            resultCached.populate(statement.executeQuery());
            return resultCached;
        }
    }

    public synchronized void executeUpdate(String query, Object... parameters) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            statement.executeUpdate();
        }
    }

    private @NotNull Connection getConnection() throws SQLException {
        try {
            Class.forName(this.driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException(driver);
        }
    }

    @Override
    public synchronized void close() {
        SQL.CONNECTIONS.remove(this);
    }
}
