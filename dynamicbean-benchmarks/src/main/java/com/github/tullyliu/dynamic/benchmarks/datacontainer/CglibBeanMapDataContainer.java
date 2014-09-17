package com.github.tullyliu.dynamic.benchmarks.datacontainer;

import com.github.tullyliu.dynamic.benchmarks.common.TestBeanDesc;
import com.github.tullyliu.dynamic.benchmarks.utils.RandomUtils;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tully on 2014/8/28.
 */
public class CglibBeanMapDataContainer implements DataContainer{

    static Class<?> cglibBean;
    static{
        BeanGenerator generator = new BeanGenerator();
        for (Map.Entry<String, Class<?>> entry : TestBeanDesc.WRAPPER_DESC.entrySet()) {
            generator.addProperty(entry.getKey(), entry.getValue());
        }
        cglibBean=(Class<?>)generator.createClass();
    }
    public Object createRow() {
        BeanMap row = null;
        try {
            row = BeanMap.create(cglibBean.newInstance());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    public Object createRows(int len) {
        List<BeanMap> list = new ArrayList<BeanMap>();
        for (int i = 0; i < len; i++) {
            list.add((BeanMap) createRow());
        }
        return list;
    }

    public Object readRows(Object rows) {
        List<BeanMap> list = (List<BeanMap>) rows;
        StringBuilder sb = new StringBuilder();
        for (BeanMap row : list) {
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
