package hexlet.code.utils;

import io.javalin.http.Context;

public class FlashUtils {
    public static void success(Context ctx, String message) {
        ctx.sessionAttribute("flash", message);
        ctx.sessionAttribute("flashType", "success");
    }

    public static void error(Context ctx, String message) {
        ctx.sessionAttribute("flash", message);
        ctx.sessionAttribute("flashType", "error");
    }
}
