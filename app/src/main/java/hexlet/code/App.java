package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class App {

    //TODO:
    /**
     * <p>4-ый шаг сделано:</p>
     * <ul>
     *     <li>1-ый пункт сделан</li>
     *     <li>2-ой пункт НЕ СДЕЛАН</li>
     *     <li>3-ий пункт сделан</li>
     *     <li>4-ый пункт сделан (но мб возможно надо ещё сделать UrlRepository)</li>
     *     <li>5-ый пункт сделан</li>
     *     <li>6-ой пункт сделан</li>
     *     <li>7-ой пункт НЕ СДЕЛАН</li>
     *     <li>8-ой пункт НЕ СДЕЛАН</li>
     * </ul>
     * **/
    public static Javalin getApp() throws SQLException {
        var hikariConfig = new HikariConfig();
        var jdbcUrl = System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:my_db; DB_CLOSE_DELAY=-1");

        hikariConfig.setJdbcUrl(jdbcUrl);

        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url)).lines().collect(Collectors.joining("\n"));

        try (var connection = dataSource.getConnection();
                var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        return Javalin.create(config -> {
           config.bundledPlugins.enableDevLogging();
           config.routes.get("/", ctx -> {
              ctx.result("Hello World and Bye!");
           });
        });
    }

    public static void main(String[] args) throws SQLException {
        int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 7070;
        getApp().start("0.0.0.0", port);
    }
}
