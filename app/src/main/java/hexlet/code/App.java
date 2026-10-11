package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

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
    public static Javalin getApp() {

        return Javalin.create(config -> {
           config.bundledPlugins.enableDevLogging();
           config.routes.get("/", ctx -> {
              ctx.result("Hello World and Bye!");
           });
        });
    }

    public static void main(String[] args) {
        int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 7070;
        getApp().start("0.0.0.0", port);
    }
}
