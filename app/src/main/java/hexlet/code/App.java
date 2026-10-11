package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static Javalin getApp() {

        return Javalin.create(config -> {
           config.bundledPlugins.enableDevLogging();
           config.routes.get("/", ctx -> {
              ctx.result("Hello World");
           });
        });
    }

    public static void main(String[] args) {
        int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 7070;
        getApp().start("0.0.0.0", port);
    }
}
