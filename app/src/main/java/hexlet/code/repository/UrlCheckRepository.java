package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck check) throws SQLException {
        var sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, NOW())";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setLong(1, check.getUrlId());
            statement.setInt(2, check.getStatusCode());
            statement.setString(3, check.getTitle());
            statement.setString(4, check.getH1());
            statement.setString(5, check.getDescription());

            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong("id"));
                check.setCreatedAt(generatedKeys.getTimestamp("created_at"));
            } else {
                throw new SQLException("Database have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> findByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        var checks = new ArrayList<UrlCheck>();
        try (var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, urlId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var check = new UrlCheck();
                check.setId(resultSet.getLong("id"));
                check.setUrlId(resultSet.getLong("url_id"));
                check.setStatusCode(resultSet.getInt("status_code"));
                check.setTitle(resultSet.getString("title"));
                check.setH1(resultSet.getString("h1"));
                check.setDescription(resultSet.getString("description"));
                check.setCreatedAt(resultSet.getTimestamp("created_at"));
                checks.add(check);
            }
        }
        return checks;
    }
}
