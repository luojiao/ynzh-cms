package com.youzan.pfcase.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <p>All rights Reserved, Designed By HQYG.</p>
 *
 * @Copyright: Copyright(C) 2016.
 * @Company: HQYG.
 * @author: luoliyuan
 * @Createdate: 2018/2/1211:36
 */
public class ObjectUtils {

    /**
     * 如果字段出现是空的，则返回false
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isNull(T t){
        if (t == null){
            return false;
        }
        Class<?> clz = t.getClass();
        for(Field field: clz.getFields()){
            int modifier = field.getModifiers();
            if (Modifier.isStatic(modifier) || Modifier.isFinal(modifier) || Modifier.isTransient(modifier)) {
                continue;
            }
            try {
                if (field.get(t) == null){
                    return false;
                }
            } catch (IllegalAccessException e) {
                return false;
            }
        }

        return true;
    }
}
