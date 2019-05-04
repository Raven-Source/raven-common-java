package org.raven.commons.data;


import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author yi.liang
 * @date 2018.9.25
 * @since JDK1.8
 */
public class ValueEnumHelper {

    private static final HashMap<String, HashMap<Integer, ValueEnum>> cache = new HashMap<>();

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends ValueEnum> HashMap<Integer, ValueEnum> getValueMap(Class<T> clazz) {

        if (clazz == null) {
            throw new IllegalArgumentException("clazz may not be null");
        }

        String key = clazz.getName();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        try {
            Method method = clazz.getMethod("values");
            ValueEnum[] inter = (ValueEnum[]) method.invoke(null);
            HashMap<Integer, ValueEnum> map = new HashMap<>(inter.length);
            for (int i = 0; i < inter.length; i++) {
                ValueEnum valueEnumType = inter[i];
                map.put(valueEnumType.getValue(), valueEnumType);
            }

            synchronized (cache) {
                if (!cache.containsKey(key)) {
                    cache.put(key, map);
                }
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends ValueEnum> T valueOf(Class<T> clazz, int value) {
        try {
            HashMap<Integer, ValueEnum> map = getValueMap(clazz);
            if (map.containsKey(value)) {
                return (T) map.get(value);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * @param clazz
     * @param name
     * @param <T>
     * @return
     */
    public static <T extends Enum & ValueEnum> T nameOf(Class<T> clazz, String name) {

        if(name == null || name.length() == 0) {
            return null;
        }

        try {
            return (T) Enum.valueOf(clazz, name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
