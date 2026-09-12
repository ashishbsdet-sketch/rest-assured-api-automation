package dev.ashish.qa.config;

public final class ApiConfig {
    private ApiConfig() {
    }

    public static String baseUrl() {
        String systemValue = System.getProperty("base.url");
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String environmentValue = System.getenv("BASE_URL");
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        return "https://jsonplaceholder.typicode.com";
    }
}
