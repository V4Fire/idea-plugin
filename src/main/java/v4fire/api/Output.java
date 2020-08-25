package v4fire.api;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class Output {
    private static final Gson gson = new Gson();

    static class Error {
        String message;
    }

    public class Response {
        boolean status;
    }

    static @NotNull Response parse(@NotNull final String output) {
        return gson.fromJson(output, Response.class);
    }
}