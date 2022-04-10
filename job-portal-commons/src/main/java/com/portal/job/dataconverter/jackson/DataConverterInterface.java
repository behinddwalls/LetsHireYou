package com.portal.job.dataconverter.jackson;

/**
 * 
 * @author pandeysp
 * 
 *         Interface for serializing and de-serializing the POJO .
 */
public interface DataConverterInterface {

    /**
     * @param data
     * @param clazz
     * @return
     * @throws Runtime
     *             Exception if not able to de-serialization Data.
     */
    public <T> T deserialize(String data, Class<T> clazz);

    /**
     * 
     * @param object
     * @return
     * @throws Runtime
     *             exception if not able to serialize the Data.
     */
    public String serialize(Object object);

}
