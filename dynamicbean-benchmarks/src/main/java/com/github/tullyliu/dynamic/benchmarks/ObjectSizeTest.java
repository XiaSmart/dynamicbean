package com.github.tullyliu.dynamic.benchmarks;

import com.github.tullyliu.dynamic.benchmarks.datacontainer.*;
import objectexplorer.MemoryMeasurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tully on 2014/8/28.
 */
public class ObjectSizeTest {
    private static List<DataContainer> datacontainers = new ArrayList<DataContainer>();
    private static int[] lens = {10000, 100000};

    public static void main(String[] args) {
        datacontainers.add(new HashMapDataContainer());
        datacontainers.add(new HardCodeBasicBeanDataContainer());
        datacontainers.add(new HardCodeWrapperBeanDataContainer());
        datacontainers.add(new ApacheDynaBeanDataContainer());
        datacontainers.add(new CglibBeanMapDataContainer());
        datacontainers.add(new DynamicBeanSetObjDataContainer());

        for (int i = 0; i < lens.length; i++) {
            System.out.println("data rows:" + lens[i]);
            for (DataContainer db : datacontainers) {
                Object obj = db.createRows(lens[i]);
                System.out.println(MemoryMeasurer.measureBytes(obj) + "  --DataContainer:" + db.getClass().getSimpleName().replace("DataContainer", ""));
                //System.out.println(ObjectGraphMeasurer.measure(obj));

            }
        }

    }
}
