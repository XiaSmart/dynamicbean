package com.github.tullyliu.dynamic.benchmarks.datacontainer;

import com.github.tullyliu.dynamic.benchmarks.common.TestBeanDesc;
import com.github.tullyliu.dynamic.benchmarks.utils.RandomUtils;
import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tully on 2014/8/28.
 */
public class ApacheDynaBeanDataContainer implements DataContainer {
    static DynaProperty[] props;

    static {
        List<DynaProperty> propsList = new ArrayList<DynaProperty>();
        for (Map.Entry<String, Class<?>> entry : TestBeanDesc.WRAPPER_DESC.entrySet()) {
            propsList.add(new DynaProperty(entry.getKey(), entry.getValue()));
        }
        props = propsList.toArray(new DynaProperty[0]);
    }

    ;
    static BasicDynaClass dynaClass = new BasicDynaClass("user", BasicDynaBean.class, props);

    public Object createRow() {
        DynaBean row = null;
        try {
            row = dynaClass.newInstance();
            row.set("dateField", new Date());
            row.set("stringField", RandomUtils.randomString());
            row.set("longField1", RandomUtils.randomLong());
            row.set("longField2", RandomUtils.randomLong());
            row.set("longField3", RandomUtils.randomLong());
            row.set("longField4", RandomUtils.randomLong());
            row.set("intField1", RandomUtils.randomInt());
            row.set("intField2", RandomUtils.randomInt());
            row.set("intField3", RandomUtils.randomInt());
            row.set("intField4", RandomUtils.randomInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    public Object createRows(int len) {
        List<DynaBean> list = new ArrayList<DynaBean>();
        for (int i = 0; i < len; i++) {
            list.add((DynaBean) createRow());
        }
        return list;
    }

    public Object readRows(Object rows) {
        List<DynaBean> list = (List<DynaBean>) rows;
        StringBuilder sb = new StringBuilder();
        for (DynaBean row : list) {
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
