package com.github.tullyliu.dynamic.benchmarks.datacontainer;

import com.github.tullyliu.dynamic.benchmarks.common.TestBasicBean;
import com.github.tullyliu.dynamic.benchmarks.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tully on 2014/8/28.
 */
public class HardCodeBasicBeanDataContainer implements DataContainer {

    public Object createRow() {
        TestBasicBean row = new TestBasicBean();
        row.setDateField(new Date());
        row.setStringField(RandomUtils.randomString());
        row.setLongField1(RandomUtils.randomLong());
        row.setLongField2(RandomUtils.randomLong());
        row.setLongField3(RandomUtils.randomLong());
        row.setLongField4(RandomUtils.randomLong());
        row.setIntField1(RandomUtils.randomInt());
        row.setIntField2(RandomUtils.randomInt());
        row.setIntField3(RandomUtils.randomInt());
        row.setIntField4(RandomUtils.randomInt());

        return row;
    }

    public Object createRows(int len) {
        List<TestBasicBean> list = new ArrayList<TestBasicBean>();
        for (int i = 0; i < len; i++) {
            list.add((TestBasicBean) createRow());
        }
        return list;
    }

    public Object readRows(Object rows) {
        List<TestBasicBean> list = (List<TestBasicBean>) rows;
        StringBuilder sb = new StringBuilder();
        for (TestBasicBean row : list) {
            sb.append(row.getDateField());
            sb.append(row.getStringField());
            sb.append(row.getLongField1());
            sb.append(row.getLongField2());
            sb.append(row.getLongField3());
            sb.append(row.getLongField4());
            sb.append(row.getIntField1());
            sb.append(row.getIntField2());
            sb.append(row.getIntField3());
            sb.append(row.getIntField4());
        }
        return sb.toString();
    }
}
