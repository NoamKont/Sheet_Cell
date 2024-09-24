package utils.deserializer;

import body.Coordinate;
import body.impl.Graph;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GraphDeserializer implements JsonDeserializer<Graph> {
    @Override
    public Graph deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Map<Coordinate, List<Coordinate>> graph = context.deserialize(json.getAsJsonObject().get("graph"), new TypeToken<Map<Coordinate, List<Coordinate>>>(){}.getType());
        Map<Coordinate, List<Coordinate>> graph_t = context.deserialize(json.getAsJsonObject().get("graph_T"), new TypeToken<Map<Coordinate, List<Coordinate>>>(){}.getType());
        return new Graph(graph, graph_t);
    }
}
