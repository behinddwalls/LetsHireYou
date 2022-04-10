package com.portal.job.dataconverter.jackson;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * 
 * @author pandeysp
 *
 *         Converts POJO to 'json' format.
 */
public class JACKSONDataConverter implements DataConverterInterface {

    private final ObjectMapper objectMapper;

    public JACKSONDataConverter() {
        // Default constructor.
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    public JACKSONDataConverter(ObjectMapper obj) {
        objectMapper = obj;
    }

    @Override
    public <T> T deserialize(final String data, final Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String serialize(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
