package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls(name, created_at) VALUES(?, NOW())";
        String sql2= "SELECT created_at FROM urls WHERE id = ?";
        try (var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, url.getName());
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else  {
                throw new SQLException("Database have not returned an id after saving an entity");
            }
            try (var statement2 = connection.prepareStatement(sql2)) {
                statement2.setLong(1, url.getId());
                try (var resultSet = statement2.executeQuery()) {
                    if (resultSet.next()) {
                        url.setCreatedAt(resultSet.getTimestamp("created_at"));
                    }
                }
            }
        }
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            if (result.next()) {
                var name =  result.getString("name");
                var createdAt = result.getTimestamp("created_at");
                var url = new Url(name, createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            var result = statement.executeQuery();
            if (result.next()) {
                var id = result.getLong("id");
                var createdAt = result.getTimestamp("created_at");
                var url = new Url(name, createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name =  resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(name, createdAt);
                url.setId(id);
                result.add(url);
            }
            return result;
        }
    }

    public static void clearAll() throws SQLException {
        try (var connection = dataSource.getConnection();
            var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM url_checks");
            statement.executeUpdate("DELETE FROM urls");

            statement.executeUpdate("ALTER TABLE  urls ALTER COLUMN id RESTART WITH 1");
            statement.executeUpdate("ALTER TABLE url_checks ALTER COLUMN id RESTART WITH 1");
        }
    }
}
