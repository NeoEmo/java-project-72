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
        getApp().start(8080);
    }
}
