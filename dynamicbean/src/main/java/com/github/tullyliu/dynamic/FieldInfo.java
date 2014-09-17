package com.github.tullyliu.dynamic;

public class FieldInfo {
    public Class<?> type;
    public String name;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
