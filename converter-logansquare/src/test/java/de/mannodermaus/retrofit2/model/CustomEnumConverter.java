package de.mannodermaus.retrofit2.model;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public class CustomEnumConverter implements TypeConverter<CustomEnum> {

    @Override
    public CustomEnum parse(JsonParser jsonParser) throws IOException {
        return CustomEnum.fromInt(jsonParser.getIntValue());
    }

    @Override
    public void serialize(CustomEnum object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeFieldName(fieldName);
        jsonGenerator.writeNumber(object.val);
    }
}
