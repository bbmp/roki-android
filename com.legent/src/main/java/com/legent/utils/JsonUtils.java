package com.legent.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Json Utils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class JsonUtils {

    protected static ObjectMapper omMapper;

    static {
        omMapper = new ObjectMapper();
        omMapper.setLocale(Locale.getDefault());

        // 忽略空字段
        omMapper.setSerializationInclusion(Include.NON_NULL);
        //
        omMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 设置默认时区
        omMapper.setTimeZone(TimeZone.getDefault());
    }

    private JsonUtils() {

    }

    public static String pojo2Json(Object pojo) throws Exception {
        String res = null;
        res = omMapper.writeValueAsString(pojo);
        return res;
    }

    public static <T> T json2Pojo(String json, Class<T> clazz) throws Exception {
        T pojo = null;
        pojo = omMapper.readValue(json, clazz);
        return pojo;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> json2List(String json, Class<T> clazz) {
        List<T> list = null;
        try {
            JavaType javaType = getCollectionType(ArrayList.class, clazz);
            list = omMapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------


    @SuppressWarnings("deprecation")
    private static JavaType getCollectionType(Class<?> collectionClass,
                                              Class<?>... elementClasses) {
        return omMapper.getTypeFactory().constructParametricType(
                collectionClass, elementClasses);
    }


    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

}
