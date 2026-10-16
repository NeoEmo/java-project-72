package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlTest {
    private static final TestConfig FOLLOW_REDIRECTS =
            new TestConfig(
                    true,
                    true,
                    HttpClient.newBuilder()
                            .followRedirects(HttpClient.Redirect.NORMAL)
                            .cookieHandler(new CookieManager()).build());

    private static Javalin app;

    @BeforeEach
    public void clear() throws SQLException {
        app = App.getApp();
        UrlRepository.clearAll();
    }

    @AfterEach
    public void tearDown() {
        if (BaseRepository.dataSource != null) {
            BaseRepository.dataSource.close();
            BaseRepository.dataSource = null;
        }
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testWrongPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/9999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void UrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void CreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=https://example.com");
            assertThat(response.code()).isEqualTo(302);
        });
    }

    @Test
    public void redirectToUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://example.com");
            UrlRepository.save(url);
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    public void redirectToUrlPage2() {
        JavalinTest.test(app, FOLLOW_REDIRECTS, (server, client) -> {
            var requestBody = "url=https://example.com";
            var response = client.post("/urls/", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    public void wrongUrlPage() {
        JavalinTest.test(app, FOLLOW_REDIRECTS, (server, client) -> {
            var requestBody = "url=lalalala";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(422);
            assertThat(response.body().string()).contains("Некорректный URL");
        });
    }

    @Test
    public void redirectsFromPages() {
        JavalinTest.test(app, (server, client) -> {
           var url = new Url("https://example.com");
           var url2 = new Url("https://example2.com");
           UrlRepository.save(url);
           UrlRepository.save(url2);
           var response = client.get("/urls");
           assertThat(response.code()).isEqualTo(200);
           assertThat(response.body().string()).contains("https://example.com");
           assertThat(response.body().string()).contains("https://example2.com");

           var response2 = client.get("/urls/" + url.getId());
           var id = 1L;
           assertThat(id).isEqualTo(url.getId());
           assertThat(response2.code()).isEqualTo(200);
           assertThat(response2.body().string()).contains(Long.toString(id));

           var response3 = client.get("/urls/" + url2.getId());
           var id2 = 2L;
           assertThat(id2).isEqualTo(url2.getId());
           assertThat(response3.code()).isEqualTo(200);
           assertThat(response3.body().string()).contains(Long.toString(id2));
        });
    }

    @Test
    public void tryDuplicatedUrl() {
        JavalinTest.test(app, FOLLOW_REDIRECTS, (server, client) -> {
            var responseBody = "url=https://example.com";
            var firstCreate = client.post("/urls", responseBody);
            var response = client.post("/urls", responseBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Страница уже существует");
        });
    }

    @Test
    public void saveUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=https://example.com");
        });

        var saved = UrlRepository.findByName("https://example.com");
        assertThat(saved).isPresent();
    }
}
