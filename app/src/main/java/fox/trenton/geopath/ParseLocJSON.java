package fox.trenton.geopath;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by trenton on 3/7/16.
 */
public class ParseLocJSON {
    private String json;

    public ParseLocJSON(String json) {
        this.json = json;
    }

    protected List<CustomLocation> parseJSON() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();
        Type listType = new TypeToken<List<CustomLocation>>() {}.getType();
        List<CustomLocation> locations = gson.fromJson(json, listType);

        return locations;
    }
}
