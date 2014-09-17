package com.github.tullyliu.dynamic.benchmarks.datacontainer;

/**
 * Created by tully on 2014/8/28.
 */
public interface DataContainer {
    Object createRow();

    Object createRows(int len);

    Object readRows(Object rows);
}
