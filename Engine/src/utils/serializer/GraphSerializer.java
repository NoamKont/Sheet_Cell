package utils.serializer;

import body.impl.Graph;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GraphSerializer implements JsonSerializer<Graph> {

    @Override
    public JsonElement serialize(Graph src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("graph", context.serialize(src.getGraph()));
        jsonObject.add("graph_T", context.serialize(src.getGraph_T()));
        return jsonObject;
    }
}
