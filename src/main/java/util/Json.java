package util;

import com.google.gson.Gson;

public class Json {
    public static String encode(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object decode(Object obj, String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, obj.getClass());
    }
}
