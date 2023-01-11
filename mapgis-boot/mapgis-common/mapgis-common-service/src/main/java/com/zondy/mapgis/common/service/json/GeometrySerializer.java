package com.zondy.mapgis.common.service.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

/**
 * GeoJSON序列化
 *
 * @author Chelsea
 * @since 2023/1/10 16:58
 */
public class GeometrySerializer extends JsonSerializer<Geometry> {
    @Override
    public void serialize(Geometry value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        // double的精度为15-16位，20位精度会导致double类型数据转json数据时精度丢失
        GeometryJSON json = new GeometryJSON(16);
        jgen.writeRawValue(json.toString(value));
    }
}
