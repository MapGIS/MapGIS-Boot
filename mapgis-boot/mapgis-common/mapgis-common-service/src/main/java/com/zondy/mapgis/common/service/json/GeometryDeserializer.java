package com.zondy.mapgis.common.service.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

/**
 * GeoJSON反序列化
 *
 * @author Chelsea
 * @since 2023/1/10 16:58
 */
public class GeometryDeserializer extends JsonDeserializer<Geometry> {
    @Override
    public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        // double的精度为15-16位，20位精度会导致double类型数据转json数据时精度丢失
        GeometryJSON json = new GeometryJSON(16);
        TreeNode str = jsonParser.readValueAsTree();
        return json.read(str.toString());
    }
}
