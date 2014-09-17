package com.github.tullyliu.dynamic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by tully on 2014/8/27.
 */
public class DynamicBeanTest {
    Map<String, Class<?>> desc = new HashMap<String, Class<?>>();

    @Before
    public void setup() {

        Class<?>[] clazztypes = new Class<?>[]{Boolean.class, Short.class, Integer.class, Long.class,
                Character.class, Double.class, Float.class, Byte.class, String.class, Date.class};
        for (int i = 0; i < clazztypes.length; i++) {
            desc.put(clazztypes[i].getSimpleName().toLowerCase() + "Field", clazztypes[i]);
        }
    }

    @Test
    public void testFieldDefine() {
        try {
            List<String> list = new ArrayList<String> ();
            DynamicBean row = DynamicBean.getClass(desc).newInstance();
            for (Field field : row.getClass().getFields()) {
                list.add(field.getName() + ":" + field.getType().getSimpleName() + ",");
            }
            Collections.sort(list);
            StringBuffer sb=new StringBuffer();
            for(String item:list){
                sb.append(item);
            }
            System.out.println(sb.toString());
            Assert.assertEquals(sb.toString(), "booleanField:boolean,byteField:byte,characterField:char,dateField:Date,doubleField:double,floatField:float,integerField:int,longField:long,shortField:short,stringField:String,");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBasicSetGet() {
        try {
            DynamicBean row = DynamicBean.getClass(desc).newInstance();
            row.set("characterField", 'x');
            row.set("floatField", 2.3f);
            row.set("doubleField", 2.4d);
            short s = 42;
            row.set("shortField", s);
            row.set("longField", 19999999l);
            row.set("integerField", 2014);
            row.set("booleanField", true);
            row.set("stringField", "dynamic");
            row.set("dateField", getSpecialDate());
            byte b = 2;
            row.set("byteField", b);
            StringBuffer sb = new StringBuffer();
            sb.append(row.get("characterField"));
            sb.append(",");
            sb.append(row.get("floatField"));
            sb.append(",");
            sb.append(row.get("doubleField"));
            sb.append(",");
            sb.append(row.get("shortField"));
            sb.append(",");
            sb.append(row.get("longField"));
            sb.append(",");
            sb.append(row.get("integerField"));
            sb.append(",");
            sb.append(row.get("booleanField"));
            sb.append(",");
            sb.append(row.get("stringField"));
            sb.append(",");
            sb.append(row.get("dateField"));
            sb.append(",");
            sb.append(row.get("byteField"));
            System.out.println(sb.toString());
            Assert.assertEquals(sb.toString(), "x,2.3,2.4,42,19999999,2014,true,dynamic,Sat Feb 01 00:00:00 CST 2014,2");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTypeSetGet() {
        try {
            DynamicBean row = DynamicBean.getClass(desc).newInstance();
            row.setChar("characterField", 'x');
            row.setFloat("floatField", 2.3f);
            row.setDouble("doubleField", 2.4d);
            short s = 42;
            row.setShort("shortField", s);
            row.setLong("longField", 19999999l);
            row.setInt("integerField", 2014);
            row.setBoolean("booleanField", true);
            row.setString("stringField", "dynamic");
            row.setDate("dateField", getSpecialDate());
            byte b = 2;
            row.setByte("byteField", b);
            StringBuffer sb = new StringBuffer();
            sb.append(row.getChar("characterField"));
            sb.append(",");
            sb.append(row.getFloat("floatField"));
            sb.append(",");
            sb.append(row.getDouble("doubleField"));
            sb.append(",");
            sb.append(row.getShort("shortField"));
            sb.append(",");
            sb.append(row.getLong("longField"));
            sb.append(",");
            sb.append(row.getInt("integerField"));
            sb.append(",");
            sb.append(row.getBoolean("booleanField"));
            sb.append(",");
            sb.append(row.getString("stringField"));
            sb.append(",");
            sb.append(row.getDate("dateField"));
            sb.append(",");
            sb.append(row.getByte("byteField"));
            System.out.println(sb.toString());
            Assert.assertEquals(sb.toString(), "x,2.3,2.4,42,19999999,2014,true,dynamic,Sat Feb 01 00:00:00 CST 2014,2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testException() {
        try {
            DynamicBean row = DynamicBean.getClass(desc).newInstance();
            row.set("longField", "test");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e instanceof ClassCastException);
        }
        try {
            DynamicBean row = DynamicBean.getClass(desc).newInstance();
            row.set("longsField", "test");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            DynamicBean row = DynamicBean.getClass(null).newInstance();
            row.set("longsField", "test");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    private Date getSpecialDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 1, 1, 0, 0, 0);
        return cal.getTime();
    }
}
