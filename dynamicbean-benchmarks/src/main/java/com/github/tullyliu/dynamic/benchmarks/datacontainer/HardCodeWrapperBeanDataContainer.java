package com.github.tullyliu.dynamic.benchmarks.datacontainer;

import com.github.tullyliu.dynamic.benchmarks.common.TestWrapperBean;
import com.github.tullyliu.dynamic.benchmarks.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tully on 2014/8/28.
 */
public class HardCodeWrapperBeanDataContainer implements DataContainer {
    public Object createRow() {
        TestWrapperBean row = new TestWrapperBean();
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
        List<TestWrapperBean> list = new ArrayList<TestWrapperBean>();
        for (int i = 0; i < len; i++) {
            list.add((TestWrapperBean) createRow());
        }
        return list;
    }

    public Object readRows(Object rows) {
        List<TestWrapperBean> list = (List<TestWrapperBean>) rows;
        StringBuilder sb = new StringBuilder();
        for (TestWrapperBean row : list) {
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
