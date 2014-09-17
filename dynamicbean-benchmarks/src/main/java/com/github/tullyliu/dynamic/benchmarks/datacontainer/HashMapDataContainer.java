package com.github.tullyliu.dynamic.benchmarks.datacontainer;

import com.github.tullyliu.dynamic.benchmarks.utils.RandomUtils;

import java.util.*;

/**
 * Created by tully on 2014/8/28.
 */
public class HashMapDataContainer implements DataContainer {

    public Object createRow() {
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("dateField", new Date());
        row.put("stringField", RandomUtils.randomString());
        row.put("longField1", RandomUtils.randomLong());
        row.put("longField2", RandomUtils.randomLong());
        row.put("longField3", RandomUtils.randomLong());
        row.put("longField4", RandomUtils.randomLong());
        row.put("intField1", RandomUtils.randomInt());
        row.put("intField2", RandomUtils.randomInt());
        row.put("intField3", RandomUtils.randomInt());
        row.put("intField4", RandomUtils.randomInt());

        return row;
    }

    public Object createRows(int len) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < len; i++) {
            list.add((Map<String, Object>) createRow());
        }
        return list;
    }

    public Object readRows(Object rows) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) rows;
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> row : list) {
            sb.append(row.get("dateField"));
            sb.append(row.get("stringField"));
            sb.append(row.get("longField1"));
            sb.append(row.get("longField2"));
            sb.append(row.get("longField3"));
            sb.append(row.get("longField4"));
            sb.append(row.get("intField1"));
            sb.append(row.get("intField2"));
            sb.append(row.get("intField3"));
            sb.append(row.get("intField4"));
        }
        return sb.toString();
    }
}
