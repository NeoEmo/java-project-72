package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

public class UrlController {
    public static void showAll(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/showAll.jte", Map.of("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("URL not found"));

        var checks = UrlCheckRepository.findByUrlId(id);

        var page = new UrlPage(url, checks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/show.jte", Map.of("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var rawUrl = ctx.formParam("url");
        if (rawUrl == null || rawUrl.isBlank()) {
            renderInvalid(ctx);
            return;
        }

        URI uri;
        try {
            uri = new URI(rawUrl.trim());
            uri.toURL();
        } catch (Exception e) {
            renderInvalid(ctx);
            return;
        }

        var scheme = uri.getScheme();
        var host = uri.getHost();
        if (scheme == null || host == null) {
            renderInvalid(ctx);
            return;
        }

        var port = uri.getPort();
        var normalize = scheme + "://" + host + (port == -1 ? "" : ":" + port);

        var isPresent = UrlRepository.findByName(normalize);
        if (isPresent.isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.redirect(NamedRoutes.getUrlIdPath(isPresent.get().getId()));
            return;
        }

        var url = new Url(normalize);
        UrlRepository.save(url);
        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.redirect(NamedRoutes.getUrlIdPath(url.getId()));
    }

    private static void renderInvalid(Context ctx) {
        var page = new BasePage();
        page.setFlash("Некорректный URL");
        ctx.status(422);
        ctx.render("index.jte", Map.of("page", page));
    }
}
