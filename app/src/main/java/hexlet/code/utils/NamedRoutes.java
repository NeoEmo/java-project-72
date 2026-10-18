package hexlet.code.utils;

public class NamedRoutes {
    public static String getUrlIdPath(Long id) {
        return "/urls/" + id;
    }

    public static String getUrlIdPath() {
        return "/urls/{id}";
    }

    public static String getUrlPath() {
        return "/urls";
    }

    public static String getUrlIdChecksPath(Long id) {
        return "/urls/" + id +  "/checks";
    }

    public static String getUrlIdChecksPath() {
        return "/urls/{id}/checks";
    }
}
