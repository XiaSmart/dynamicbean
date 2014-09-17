package com.github.tullyliu.dynamic;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * Dynamic Bean Using ASM Memory Save and High Performance
 */
public abstract class DynamicBean {
    /**
     * set field by value
     *
     * @param fieldName
     * @param value
     */
    public abstract void set(String fieldName, Object value);

    /**
     * get field
     *
     * @param fieldName
     * @return
     */
    public abstract Object get(String fieldName);


    public abstract void setString(String fieldName, String value);

    public abstract void setDate(String fieldName, Date value);

    public abstract void setBoolean(String fieldName, boolean value);

    public abstract void setByte(String fieldName, byte value);

    public abstract void setShort(String fieldName, short value);

    public abstract void setInt(String fieldName, int value);

    public abstract void setLong(String fieldName, long value);

    public abstract void setDouble(String fieldName, double value);

    public abstract void setFloat(String fieldName, float value);

    public abstract void setChar(String fieldName, char value);

    public abstract String getString(String fieldName);

    public abstract Date getDate(String fieldName);

    public abstract char getChar(String fieldName);

    public abstract boolean getBoolean(String fieldName);

    public abstract byte getByte(String fieldName);

    public abstract short getShort(String fieldName);

    public abstract int getInt(String fieldName);

    public abstract long getLong(String fieldName);

    public abstract double getDouble(String fieldName);

    public abstract float getFloat(String fieldName);

    /**
     * class cache
     */
    private final static ConcurrentHashMap<String, Class<? extends DynamicBean>> DYNAMIC_BEAN_CLASS_MAP = new ConcurrentHashMap<String, Class<? extends DynamicBean>>();

    /**
     * Wrapper Class To Primitive Class Map
     */
    @SuppressWarnings("serial")
    private static Map<Class<?>, Class<?>> WrapperToPrimitive = new HashMap<Class<?>, Class<?>>() {
        {
            put(Long.class, long.class);
            put(Integer.class, int.class);
            put(Short.class, short.class);
            put(Float.class, float.class);
            put(Byte.class, byte.class);
            put(Double.class, double.class);
            put(Character.class, char.class);
            put(Boolean.class, boolean.class);
        }
    };

    /**
     * Transfer wrapper class to primitive class
     *
     * @param clazz clazz to transfer
     * @return class info after transfer
     */
    private static Class<?> clazzToPrimitives(Class<?> clazz) {
        Class<?> clazzName = WrapperToPrimitive.get(clazz);
        if (clazzName == null) {
            return clazz;
        } else {
            return clazzName;
        }
    }

    /**
     * Get classloader from thread context
     *
     * @return ClassLoader
     */
    private static ClassLoader getClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader;
        }
        return DynamicBean.class.getClassLoader();
    }

    /**
     * Define asm generated class to classloader
     *
     * @param className  ClassName to define
     * @param classBytes ClassBytes
     * @return ClassInfo
     */
    private static Class<?> defineClass(String className, byte[] classBytes) {
        // override classDefine (as it is protected) and define the class.
        Class<?> clazz = null;
        try {
            ClassLoader loader = getClassLoader();
            java.lang.reflect.Method method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{
                    String.class, byte[].class, int.class, int.class});

            // protected method invocaton
            method.setAccessible(true);
            try {
                Object[] args = new Object[]{className, classBytes, 0, classBytes.length};
                clazz = (Class<?>) method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return clazz;
    }

    /**
     * Get or create class
     *
     * @param fieldDefinition field definition map
     * @return generated class
     */
    public static Class<? extends DynamicBean> getClass(Map<String, Class<?>> fieldDefinition) {
        if (fieldDefinition == null || fieldDefinition.isEmpty()) {
            throw new IllegalArgumentException("Fields Definition can not be null or empty.");
        }
        String accessClassName = DynamicBean.class.getName() + fieldDefinition.hashCode();
        Class<? extends DynamicBean> rec = DYNAMIC_BEAN_CLASS_MAP.get(accessClassName);
        if (rec == null) {
            synchronized (DynamicBean.class) {
                rec = DYNAMIC_BEAN_CLASS_MAP.get(accessClassName);
                if (rec == null) {
                    Class<? extends DynamicBean> newRec = createClass(fieldDefinition);
                    rec = DYNAMIC_BEAN_CLASS_MAP.putIfAbsent(accessClassName, newRec);
                    if (rec == null) {
                        rec = newRec;
                    }
                }
            }
        }
        return rec;
    }

    /**
     * Get DynamicBean Class using asm.
     *
     * @param props decription for dynamic class
     * @return ClassInfo
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends DynamicBean> createClass(Map<String, Class<?>> props) {
        String accessClassName = DynamicBean.class.getName() + props.hashCode();
        ArrayList<FieldInfo> fields = new ArrayList<FieldInfo>();
        for (Map.Entry<String, Class<?>> entry : props.entrySet()) {
            FieldInfo field = new FieldInfo();
            field.setName(entry.getKey());
            field.setType(clazzToPrimitives(entry.getValue()));
            fields.add(field);
        }
        String accessClassNameInternal = accessClassName.replace('.', '/');

        ClassWriter cw = new ClassWriter(0);
//        PrintWriter writer = new PrintWriter(System.out);
//        CheckClassAdapter cca = new CheckClassAdapter(cwi);
//        TraceClassVisitor cw = new TraceClassVisitor(cca, writer);

        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, accessClassNameInternal, null, Type.getInternalName(DynamicBean.class),
                null);
        insertField(cw, fields);
        insertConstructor(cw);
        insertGetObject(cw, accessClassNameInternal, fields);
        insertSetObject(cw, accessClassNameInternal, fields);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.BOOLEAN_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.BOOLEAN_TYPE);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.BYTE_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.BYTE_TYPE);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.SHORT_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.SHORT_TYPE);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.INT_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.INT_TYPE);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.LONG_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.LONG_TYPE);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.DOUBLE_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.DOUBLE_TYPE);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.FLOAT_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.FLOAT_TYPE);
        insertGetPrimitive(cw, accessClassNameInternal, fields, Type.CHAR_TYPE);
        insertSetPrimitive(cw, accessClassNameInternal, fields, Type.CHAR_TYPE);
        insertGetSpecialType(cw, accessClassNameInternal, fields, String.class);
        insertSetSpecialType(cw, accessClassNameInternal, fields, String.class);
        insertGetSpecialType(cw, accessClassNameInternal, fields, Date.class);
        insertSetSpecialType(cw, accessClassNameInternal, fields, Date.class);
        cw.visitEnd();
        return (Class<? extends DynamicBean>) defineClass(accessClassName, cw.toByteArray());

    }

    private static void insertConstructor(ClassVisitor cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, DynamicBean.class.getName().replace('.', '/'), "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private static void insertField(ClassVisitor cw, ArrayList<FieldInfo> fields) {
        for (FieldInfo field : fields) {
            cw.visitField(ACC_PUBLIC, field.getName(), Type.getDescriptor(field.getType()), null, null).visitEnd();
        }
    }

    private static void insertSetObject(ClassVisitor cw, String classNameInternal, ArrayList<FieldInfo> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "set", "(Ljava/lang/String;Ljava/lang/Object;)V", null, null);
        mv.visitCode();
        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            for (int i = 0, n = labels.length; i < n; i++) {
                labels[i] = new Label();
            }
            for (int i = 0, n = labels.length; i < n; i++) {
                FieldInfo field = fields.get(i);
                Type fieldType = Type.getType(field.getType());

                mv.visitVarInsn(ALOAD, 1);
                mv.visitLdcInsn(field.getName());
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                mv.visitJumpInsn(IFEQ, labels[i]);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 2);

                switch (fieldType.getSort()) {
                    case Type.BOOLEAN:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                        break;
                    case Type.BYTE:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                        break;
                    case Type.CHAR:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
                        break;
                    case Type.SHORT:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                        break;
                    case Type.INT:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                        break;
                    case Type.FLOAT:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                        break;
                    case Type.LONG:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
                        break;
                    case Type.DOUBLE:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                        break;
                    case Type.ARRAY:
                        mv.visitTypeInsn(CHECKCAST, fieldType.getDescriptor());
                        break;
                    case Type.OBJECT:
                        mv.visitTypeInsn(CHECKCAST, fieldType.getInternalName());
                        break;
                }

                mv.visitFieldInsn(PUTFIELD, classNameInternal, field.getName(), fieldType.getDescriptor());
                mv.visitInsn(RETURN);
                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);
            }
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static void insertGetObject(ClassVisitor cw, String classNameInternal, ArrayList<FieldInfo> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get", "(Ljava/lang/String;)Ljava/lang/Object;", null, null);
        mv.visitCode();

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            for (int i = 0, n = labels.length; i < n; i++) {
                labels[i] = new Label();
            }

            for (int i = 0, n = labels.length; i < n; i++) {
                FieldInfo field = fields.get(i);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitLdcInsn(field.getName());
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                mv.visitJumpInsn(IFEQ, labels[i]);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, classNameInternal, field.getName(), Type.getDescriptor(field.getType()));

                Type fieldType = Type.getType(field.getType());
                switch (fieldType.getSort()) {
                    case Type.BOOLEAN:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;",
                                false);
                        break;
                    case Type.BYTE:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                        break;
                    case Type.CHAR:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;",
                                false);
                        break;
                    case Type.SHORT:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                        break;
                    case Type.INT:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",
                                false);
                        break;
                    case Type.FLOAT:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                        break;
                    case Type.LONG:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                        break;
                    case Type.DOUBLE:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                        break;
                }
                mv.visitInsn(ARETURN);
                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);
            }
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 2);
        mv.visitEnd();
    }

    private static void insertSetPrimitive(ClassVisitor cw, String classNameInternal, ArrayList<FieldInfo> fields,
                                           Type primitiveType) {
        int maxStack = 6;
        int maxLocals = 4; // See correction below for LLOAD and DLOAD
        final String setterMethodName;
        final String typeNameInternal = primitiveType.getDescriptor();
        final int loadValueInstruction;
        switch (primitiveType.getSort()) {
            case Type.BOOLEAN:
                setterMethodName = "setBoolean";
                loadValueInstruction = ILOAD;
                break;
            case Type.BYTE:
                setterMethodName = "setByte";
                loadValueInstruction = ILOAD;
                break;
            case Type.CHAR:
                setterMethodName = "setChar";
                loadValueInstruction = ILOAD;
                break;
            case Type.SHORT:
                setterMethodName = "setShort";
                loadValueInstruction = ILOAD;
                break;
            case Type.INT:
                setterMethodName = "setInt";
                loadValueInstruction = ILOAD;
                break;
            case Type.FLOAT:
                setterMethodName = "setFloat";
                loadValueInstruction = FLOAD;
                break;
            case Type.LONG:
                setterMethodName = "setLong";
                loadValueInstruction = LLOAD;
                maxLocals++; // (LLOAD and DLOAD actually load two slots)
                break;
            case Type.DOUBLE:
                setterMethodName = "setDouble";
                loadValueInstruction = DLOAD; // (LLOAD and DLOAD actually load
                // two slots)
                maxLocals++;
                break;
            default:
                setterMethodName = "set";
                loadValueInstruction = ALOAD;
                break;
        }
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, setterMethodName,
                "(Ljava/lang/String;" + typeNameInternal + ")V", null, null);
        mv.visitCode();

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (Type.getType(fields.get(i).getType()).equals(primitiveType))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }

            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    FieldInfo field = fields.get(i);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(field.getName());
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    mv.visitJumpInsn(IFEQ, labels[i]);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitVarInsn(loadValueInstruction, 2);
                    mv.visitFieldInsn(PUTFIELD, classNameInternal, field.getName(), typeNameInternal);
                    mv.visitInsn(RETURN);
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                }
            }
            // Rest of fields: different type
            if (hasAnyBadTypeLabel) {
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, maxLocals);
        mv.visitEnd();
    }

    static private void insertGetPrimitive(ClassVisitor cw, String classNameInternal, ArrayList<FieldInfo> fields,
                                           Type primitiveType) {
        int maxStack = 6;
        final String getterMethodName;
        final String typeNameInternal = primitiveType.getDescriptor();
        final int returnValueInstruction;
        switch (primitiveType.getSort()) {
            case Type.BOOLEAN:
                getterMethodName = "getBoolean";
                returnValueInstruction = IRETURN;
                break;
            case Type.BYTE:
                getterMethodName = "getByte";
                returnValueInstruction = IRETURN;
                break;
            case Type.CHAR:
                getterMethodName = "getChar";
                returnValueInstruction = IRETURN;
                break;
            case Type.SHORT:
                getterMethodName = "getShort";
                returnValueInstruction = IRETURN;
                break;
            case Type.INT:
                getterMethodName = "getInt";
                returnValueInstruction = IRETURN;
                break;
            case Type.FLOAT:
                getterMethodName = "getFloat";
                returnValueInstruction = FRETURN;
                break;
            case Type.LONG:
                getterMethodName = "getLong";
                returnValueInstruction = LRETURN;
                break;
            case Type.DOUBLE:
                getterMethodName = "getDouble";
                returnValueInstruction = DRETURN;
                break;
            default:
                getterMethodName = "get";
                returnValueInstruction = ARETURN;
                break;
        }
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, getterMethodName, "(Ljava/lang/String;)" + typeNameInternal,
                null, null);
        mv.visitCode();

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (Type.getType(fields.get(i).getType()).equals(primitiveType))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }

            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    FieldInfo field = fields.get(i);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(field.getName());
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    mv.visitJumpInsn(IFEQ, labels[i]);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitFieldInsn(GETFIELD, classNameInternal, field.getName(), typeNameInternal);
                    mv.visitInsn(returnValueInstruction);

                    mv.visitLabel(labels[i]);
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                }
            }
            if (hasAnyBadTypeLabel) {
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 2);
        mv.visitEnd();
    }

    private static void insertSetSpecialType(ClassVisitor cw, String classNameInternal, ArrayList<FieldInfo> fields,
                                             Class<?> clazz) {
        int maxStack = 6;
        final String setterMethodName = "set" + clazz.getSimpleName();
        final String typeNameInternal = Type.getDescriptor(clazz);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, setterMethodName,
                "(Ljava/lang/String;" + typeNameInternal + ")V", null, null);
        mv.visitCode();

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (clazz.equals(fields.get(i).getType()))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }

            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    FieldInfo field = fields.get(i);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(field.getName());
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    mv.visitJumpInsn(IFEQ, labels[i]);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitVarInsn(ALOAD, 2);
                    mv.visitFieldInsn(PUTFIELD, classNameInternal, field.getName(), typeNameInternal);
                    mv.visitInsn(RETURN);
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                }
            }
            if (hasAnyBadTypeLabel) {
                insertThrowExceptionForFieldType(mv, clazz.getName());
            }
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static void insertGetSpecialType(ClassVisitor cw, String classNameInternal, ArrayList<FieldInfo> fields,
                                             Class<?> clazz) {
        int maxStack = 6;
        final String getterMethodName = "get" + clazz.getSimpleName();
        final String typeNameInternal = Type.getDescriptor(clazz);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, getterMethodName, "(Ljava/lang/String;)" + typeNameInternal,
                null, null);
        mv.visitCode();

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (clazz.equals(fields.get(i).getType()))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    FieldInfo field = fields.get(i);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(field.getName());
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    mv.visitJumpInsn(IFEQ, labels[i]);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitFieldInsn(GETFIELD, classNameInternal, field.getName(), Type.getDescriptor(field.getType()));
                    mv.visitInsn(ARETURN);
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                }
            }
            if (hasAnyBadTypeLabel) {
                insertThrowExceptionForFieldType(mv, "String");
            }
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    static private MethodVisitor insertThrowExceptionForFieldNotFound(MethodVisitor mv) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Field not found: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V",
                false);
        mv.visitInsn(ATHROW);
        return mv;
    }

    static private MethodVisitor insertThrowExceptionForFieldType(MethodVisitor mv, String fieldType) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Field not declared as " + fieldType + ": ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V",
                false);
        mv.visitInsn(ATHROW);
        return mv;
    }

}
