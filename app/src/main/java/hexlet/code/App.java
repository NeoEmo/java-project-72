package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import hexlet.code.controller.UrlCheckController;
import hexlet.code.controller.UrlController;
import hexlet.code.dto.BasePage;
import hexlet.code.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;
import static hexlet.code.repository.BaseRepository.dataSource;

@Slf4j
public class App {
    private static final Path TEMPLATES_PATH = Path.of("src", "main", "resources", "templates");
    private static final Path STATIC_PATH = Path.of("src", "main", "resources", "static");
    private static final Path JTE_CLASSES_PATH = Path.of("jte-classes");

    private static String getMode() {
        return System.getenv().getOrDefault("APP_ENV", "production");
    }

    private static boolean isDevelopment() {
        return getMode().equals("development") && Files.isDirectory(TEMPLATES_PATH);
    }

    private static TemplateEngine createTemplateEngine() {
        if (isDevelopment()) {
            var codeResolver = new DirectoryCodeResolver(TEMPLATES_PATH);
            return TemplateEngine.create(codeResolver, JTE_CLASSES_PATH, ContentType.Html);
        }
        return TemplateEngine.createPrecompiled(ContentType.Html);
    }

    public static Javalin getApp() throws SQLException {
        var hikariConfig = new HikariConfig();
        var jdbcUrl = System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1");

        hikariConfig.setJdbcUrl(jdbcUrl);

        var dbUser = System.getenv("JDBC_DATABASE_USER");
        var dbPassword = System.getenv("JDBC_DATABASE_PASSWORD");

        if (dbUser != null && !dbUser.isEmpty()) {
            hikariConfig.setUsername(dbUser);
        }

        if (dbPassword != null && !dbPassword.isEmpty()) {
            hikariConfig.setPassword(dbPassword);
        }

        dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url)).lines().collect(Collectors.joining("\n"));

        try (var connection = dataSource.getConnection();
                var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            log.info("Running in {} mode", getMode());

            if (isDevelopment()) {
                config.staticFiles.add(STATIC_PATH.toString(), Location.EXTERNAL);
            } else {
                config.staticFiles.add("/static", Location.CLASSPATH);
            }

            config.fileRenderer(new JavalinJte(createTemplateEngine()));

            config.routes.get("/", ctx -> {
                ctx.render("index.jte", Map.of("page", new BasePage()));
            });
            config.routes.post(NamedRoutes.getUrlPath(), UrlController::create);
            config.routes.get(NamedRoutes.getUrlPath(), UrlController::showAll);
            config.routes.get(NamedRoutes.getUrlIdPath(), UrlController::show);
            config.routes.post(NamedRoutes.getUrlIdChecksPath(), UrlCheckController::create);
        });
    }

    public static void main(String[] args) throws SQLException {
        int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 7070;
        getApp().start("0.0.0.0", port);
    }
}
