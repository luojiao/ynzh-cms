package com.yunnzh.cms.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <p>All rights Reserved, Designed By YNZH.</p>
 * @Copyright:   Copyright(C) 2018.
 * @Company:     HQYG.
 * @author:      luoliyuan
 * @Createdate:  2018/2/12 16:36
 */
public class JsonUtils {

    public static String toJson(Object obj) {
        return JSON.toJSONString(obj,
                new SerializerFeature[]{
                        SerializerFeature.WriteMapNullValue,
                        SerializerFeature.DisableCircularReferenceDetect,
                        SerializerFeature.WriteNonStringValueAsString
                });
    }

    public static <T> T fromJson(String jsonString, TypeReference<T> type){
        try{
            return JSON.parseObject(jsonString, type, new Feature[0]);
        }catch(Exception e){
            return null;
        }
    }

    public static <T> T fromJson(String jsonString) throws Exception {
        return JSON.parseObject(jsonString, new TypeReference<T>() {
        }, new Feature[0]);
    }

}
