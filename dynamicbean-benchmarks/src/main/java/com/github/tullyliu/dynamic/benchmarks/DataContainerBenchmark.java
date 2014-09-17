package com.github.tullyliu.dynamic.benchmarks;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import com.github.tullyliu.dynamic.benchmarks.datacontainer.ApacheDynaBeanDataContainer;
import com.github.tullyliu.dynamic.benchmarks.datacontainer.CglibBeanMapDataContainer;
import com.github.tullyliu.dynamic.benchmarks.datacontainer.DataContainer;
import com.github.tullyliu.dynamic.benchmarks.datacontainer.DynamicBeanSetObjDataContainer;
import com.github.tullyliu.dynamic.benchmarks.datacontainer.DynamicBeanSetTypeDataContainer;
import com.github.tullyliu.dynamic.benchmarks.datacontainer.HardCodeBasicBeanDataContainer;
import com.github.tullyliu.dynamic.benchmarks.datacontainer.HardCodeWrapperBeanDataContainer;
import com.github.tullyliu.dynamic.benchmarks.datacontainer.HashMapDataContainer;

/**
 * Created by tully on 2014/8/28.
 */

@BenchmarkMode({ Mode.AverageTime })
// us 微秒10 -6 s
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class DataContainerBenchmark {
    // @Param({ "map", "pojo", "dyna", "mapdb-map", "mapdb-pojo", "dynabean",
    // "cglib" })
    @Param({ "hashmap", "hardcode-wrapper-bean", "hardcode-basic-bean", "apache-dynabean", "cglib-beanmap",
            "dynamicbean-setobj", "dynamicbean-settype" })
    public String dataContainerType;
    @Param({ "10000" })
    public int len;
    public DataContainer datacontainer;
    public Object result;

    @Setup
    public void setup() {
        if (dataContainerType.equals("hashmap")) {
            datacontainer = new HashMapDataContainer();
        } else if (dataContainerType.equals("hardcode-wrapper-bean")) {
            datacontainer = new HardCodeWrapperBeanDataContainer();
        } else if (dataContainerType.equals("hardcode-basic-bean")) {
            datacontainer = new HardCodeBasicBeanDataContainer();
        } else if (dataContainerType.equals("apache-dynabean")) {
            datacontainer = new ApacheDynaBeanDataContainer();
        } else if (dataContainerType.equals("cglib-beanmap")) {
            datacontainer = new CglibBeanMapDataContainer();
        } else if (dataContainerType.equals("dynamicbean-setobj")) {
            datacontainer = new DynamicBeanSetObjDataContainer();
        } else if (dataContainerType.equals("dynamicbean-settype")) {
            datacontainer = new DynamicBeanSetTypeDataContainer();
        }
        this.result = datacontainer.createRows(len);
    }

    @Benchmark
    public Object runCreateTest() {
        try {
            Object obj = datacontainer.createRows(len);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Benchmark
    public Object runReadTest() {
        try {
            Object obj = datacontainer.readRows(this.result);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TearDown
    public void teardown() {
    }
}