package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.FlashUtils;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLException;

public class UrlCheckController {
    public static void create(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id).orElseThrow(() -> new NotFoundResponse("URL not Found"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName())
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                            + "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .asString();
            var statusCode = response.getStatus();

            if (statusCode >= 400) {
                FlashUtils.error(ctx, "Произошла ошибка при проверке");
                ctx.redirect(NamedRoutes.getUrlIdPath(id));
                return;
            }

            Document document = Jsoup.parse(response.getBody());
            var title = document.title();
            var h1Element = document.selectFirst("h1");
            var h1 = h1Element != null ? h1Element.text() : "";
            var descElement = document.selectFirst("meta[name=description]");
            var description = descElement != null ? descElement.attr("content") : "";

            var check = new UrlCheck();
            check.setUrlId(id);
            check.setStatusCode(statusCode);
            check.setTitle(title);
            check.setH1(h1);
            check.setDescription(description);

            UrlCheckRepository.save(check);
            FlashUtils.success(ctx, "Страница успешно проверена");
        } catch (Exception e) {
            /*
                  Мемориал памяти имени e.printStackTrace().
                  В памяти тех, кто пушил и надеялся увидеть лог почему не проходит hexlet-check.
                  И тех кто увидел зелёный CI.
                  Ты заслужил отдых.
                  20.10.2026-22.10.2026.
             */
            FlashUtils.error(ctx, "Произошла ошибка при проверке");
        }
        ctx.redirect(NamedRoutes.getUrlIdPath(id));
    }
}
