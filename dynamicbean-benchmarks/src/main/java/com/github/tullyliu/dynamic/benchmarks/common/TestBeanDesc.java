package com.github.tullyliu.dynamic.benchmarks.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestBeanDesc {
    public static Map<String, Class<?>> WRAPPER_DESC = new HashMap<String, Class<?>>() {
        {
            put("dateField", Date.class);
            put("stringField", String.class);
            put("longField1", Long.class);
            put("longField2", Long.class);
            put("longField3", Long.class);
            put("longField4", Long.class);
            put("intField1", Integer.class);
            put("intField2", Integer.class);
            put("intField3", Integer.class);
            put("intField4", Integer.class);

        }
    };
    public static Map<String, Class<?>> BASIC_DESC = new HashMap<String, Class<?>>() {
        {
            put("dateField", Date.class);
            put("stringField", String.class);
            put("longField1", long.class);
            put("longField2", long.class);
            put("longField3", long.class);
            put("longField4", long.class);
            put("intField1", int.class);
            put("intField2", int.class);
            put("intField3", int.class);
            put("intField4", int.class);

        }
    };
}
