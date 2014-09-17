## DynamicBean是什么
DynamicBean是一个基于ASM实现的动态JavaBean,可以实现完全替换Map<String,Object>用于系统里动态数据存储,拥有跟hardcode javabean一致的内存占用和读写性能
## 常见实现
1. Apache BeanUtils的DynaBean
2. CGLIB的BeanMap

## 使用方法
编译
```
mvn clean install
```
加入Maven依赖
```xml
<dependency>
   <groupId>com.github.tullyliu.dynamic</groupId>
   <artifactId>dynamicbean</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
调用方法
SetGet By Object
```java
Map<String, Class<?>> desc = new HashMap<String, Class<?>>() {
    {
        put("dateField", Date.class);
        put("stringField", String.class);
        put("longField", Long.class);
        put("intField", Integer.class);
    }
};
// create class
Class<? extends DynamicBean> clazz = DynamicBean.getClass(desc);
// create instance
DynamicBean row = clazz.newInstance();
// set value by object
row.set("dateField", new Date());
row.set("stringField", "Tom");
row.set("longField", 20140000l);
row.set("intField", 2014);
// get value by object
Date rowDate = (Date) row.get("dateField");
String rowString = (String) row.get("stringField");
long rowLong = (Long) row.get("longField");
int rowInt = (Integer) row.get("intField");
// set value by type
row.setDate("dateField", rowDate);
row.setString("stringField", rowString);
row.setLong("longField", rowLong);
row.setInt("intField", rowInt);
// //get value by type
StringBuffer sb = new StringBuffer();
sb.append(row.getDate("dateField"));
sb.append(row.getString("stringField"));
sb.append(row.getLong("longField"));
sb.append(row.getInt("intField"));

System.out.println(sb.toString());
```
## 性能比较
### 测试环境
1. CPU:Intel(R) Xeon(R) CPU E5-2620 @ 2.00GHz 6 cores, 
2. Memory:64GB RAM
3. OS:RHEL4.3 & CentOS 6.3

### 测试方法
读写10000/10000个10字段(1个String,1个Date,4个Long,4个Integer,随机填充值)的JavaBean
写的方式为随机生成值填入Java对象,然后放入ArrayList
读的方式为遍历List,读取Java对象,打印到StringBuffer中
测试的实现如下:

1. hardcode-basic-bean 硬编码JavaBean,使用int.long基本类型
1. hardcode-wrapper-bean 硬编码JavaBean,使用Integer,Long等包装类型
1. dynamicbean-settype DynamicBean实现,使用setInt,setLong等带类型的方法
1. dynamicbean-setobj DynamicBean实现,使用set Object方法
1. hashmap Map<String,Object>实现
1. cglib-beanmap CGLIB的BeanMap实现
1. apache-dynabean Apache的DynaBean实现

#### 内存占用
![memory](http://dynamicbean.qiniudn.com/memory.jpg)
#### 读写性能(RHEL4.3)
![RHEL.4.3ReadWrite](http://dynamicbean.qiniudn.com/RHEL.4.3ReadWrite.jpg)
#### 读写性能(CentOS 6.3)
![CentOS6.3ReadWrite](http://dynamicbean.qiniudn.com/CentOS6.3ReadWrite.jpg)
