package v4fire.api;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class Output {
    private static final Gson gson = new Gson();

    protected class Data {
        @Nullable
        String message;
    }

    public class Response {
        boolean status;
        @Nullable
        Data data;
    }

    static @NotNull Response parse(@NotNull final String output) {
        return gson.fromJson(output, Response.class);
    }
}