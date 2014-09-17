package com.github.tullyliu.dynamic.benchmarks.datacontainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.tullyliu.dynamic.DynamicBean;
import com.github.tullyliu.dynamic.benchmarks.common.TestBeanDesc;
import com.github.tullyliu.dynamic.benchmarks.utils.RandomUtils;

/**
 * Created by tully on 2014/8/28.
 */
public class DynamicBeanSetTypeDataContainer implements DataContainer {
    static Class<? extends DynamicBean> clazz = DynamicBean.getClass(TestBeanDesc.WRAPPER_DESC);

    public Object createRow() {
        DynamicBean row = null;
        try {
            row = clazz.newInstance();
            row.setDate("dateField", new Date());
            row.setString("stringField", RandomUtils.randomString());
            row.setLong("longField1", RandomUtils.randomLong());
            row.setLong("longField2", RandomUtils.randomLong());
            row.setLong("longField3", RandomUtils.randomLong());
            row.setLong("longField4", RandomUtils.randomLong());
            row.setInt("intField1", RandomUtils.randomInt());
            row.setInt("intField2", RandomUtils.randomInt());
            row.setInt("intField3", RandomUtils.randomInt());
            row.setInt("intField4", RandomUtils.randomInt());
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
            sb.append(row.getDate("dateField"));
            sb.append(row.getString("stringField"));
            sb.append(row.getLong("longField1"));
            sb.append(row.getLong("longField2"));
            sb.append(row.getLong("longField3"));
            sb.append(row.getLong("longField4"));
            sb.append(row.getInt("intField1"));
            sb.append(row.getInt("intField2"));
            sb.append(row.getInt("intField3"));
            sb.append(row.getInt("intField4"));
        }
        return sb.toString();
    }
}
