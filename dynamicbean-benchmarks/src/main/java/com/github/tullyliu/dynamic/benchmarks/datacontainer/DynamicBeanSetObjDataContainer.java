package com.github.tullyliu.dynamic.benchmarks.datacontainer;

import com.github.tullyliu.dynamic.DynamicBean;
import com.github.tullyliu.dynamic.benchmarks.common.TestBeanDesc;
import com.github.tullyliu.dynamic.benchmarks.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tully on 2014/8/28.
 */
public class DynamicBeanSetObjDataContainer implements DataContainer{
    static Class<? extends DynamicBean> clazz = DynamicBean.getClass(TestBeanDesc.WRAPPER_DESC);

    public Object createRow() {
        DynamicBean row = null;
        try {
            row =  clazz.newInstance();
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
        List<DynamicBean> list = new ArrayList<DynamicBean>();
        for (int i = 0; i < len; i++) {
            list.add((DynamicBean) createRow());
        }
        return list;
    }

    public Object readRows(Object rows) {
        List<DynamicBean> list = (List<DynamicBean>) rows;
        StringBuilder sb = new StringBuilder();
        for (DynamicBean row : list) {
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
