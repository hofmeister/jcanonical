package com.vonhof.jcanonical;

import org.jannocessor.collection.api.PowerList;
import org.jannocessor.model.bean.structure.JavaClassBean;
import org.jannocessor.model.executable.JavaConstructor;
import org.jannocessor.model.executable.JavaMethod;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.model.structure.JavaMetadata;
import org.jannocessor.model.type.JavaType;
import org.jannocessor.model.util.Classes;
import org.jannocessor.model.util.Constructors;
import org.jannocessor.model.util.Fields;
import org.jannocessor.model.util.Methods;
import org.jannocessor.model.util.New;
import org.jannocessor.model.variable.JavaField;
import org.jannocessor.processor.api.CodeProcessor;
import org.jannocessor.processor.api.ProcessingContext;
import org.jannocessor.proxy.JavaDeclaredTypeProxy;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CanonicalProcessor implements CodeProcessor<JavaClass> {

    private static final Map<String,String> primitiveToClass = new HashMap<String, String>();

    static {
        primitiveToClass.put("int","Integer");
        primitiveToClass.put("long","Long");
        primitiveToClass.put("short","Short");
        primitiveToClass.put("byte","Byte");
        primitiveToClass.put("float","Float");
        primitiveToClass.put("double","Double");
        primitiveToClass.put("boolean","Boolean");
        primitiveToClass.put("char","Character");
    }

    private JavaClass generateClass(JavaClass original, ProcessingContext context) {

        final JavaType targetClass = getTargetClass(original);

        if (targetClass == null) {
            context.getLogger().error("Class did not have annotation: {}", original.getName());
            return null;
        }


        final String className = original.getName().appendPart("Accessor").getCapitalized();

        final JavaType superType = New.type("com.vonhof.jcanonical.Accessor<%s>", targetClass.getCanonicalName());

        final String qualifiedClassName = String.format("%s.%s",original.getParent().getName().getText(),className);

        final JavaType accessorType = New.type(qualifiedClassName);

        final JavaClassBean accessorClass = (JavaClassBean) New.classs(Classes.PUBLIC, className, superType);
        accessorClass.setType(accessorType);

        accessorClass.setParent(original.getParent());


        final JavaConstructor constructor = New.constructor(Constructors.PUBLIC, New.parameter(targetClass,"delegate"));

        accessorClass.getConstructors().add(constructor);

        StringBuilder constructorBody = new StringBuilder();

        constructorBody.append("super(delegate);\n");

        for(JavaField field : original.getFields()) {

            String canonicalPath = getAnnotationValue(field.getMetadata(), CanonicalField.class, String.class);
            if (canonicalPath == null) {
                continue;
            }
            JavaField accessorField = makeField(field, targetClass);
            accessorClass.getFields().add(accessorField);

            accessorClass.getMethods().add(makeGetter(field));
            accessorClass.getMethods().add(makeSetter(field, accessorType));

            constructorBody.append(makeConstructorInit(field, targetClass, canonicalPath)).append("\n");

        }

        constructor.getBody().setHardcoded(constructorBody.toString());

        return accessorClass;
    }

    private JavaType getTargetClass(JavaClass original) {
        return New.type(getAnnotationValue(original.getMetadata(), Canonical.class, JavaDeclaredTypeProxy.class).getCanonicalName());
    }

    private String makeConstructorInit(JavaField field, JavaType targetClass, String path) {
        final String fieldName = field.getName().getUncapitalized();

        return String.format("this.%s = new FieldAccessor<%s,%s>(delegate,\"%s\");",
                fieldName, targetClass.getCanonicalName(), getFieldType(field).getCanonicalName(), path);
    }

    private JavaField makeField(JavaField field, JavaType targetClass) {
        final String fieldName = field.getName().getUncapitalized();

        JavaType fieldAccessorType = New.type("com.vonhof.jcanonical.FieldAccessor<%s,%s>", targetClass.getCanonicalName(), getFieldType(field).getCanonicalName());
        return New.field(Fields.PRIVATE_FINAL, fieldAccessorType, fieldName);
    }

    private JavaType getFieldType(JavaField field) {
        if (field.getType().getKind().isPrimitive()) {
            String name = field.getType().getSimpleName().getText().toLowerCase();
            return New.type("java.lang.%s", primitiveToClass.get(name));
        }
        return field.getType();
    }


    private JavaMethod makeSetter(JavaField field, JavaType accessorClass) {

        final String fieldName = field.getName().getUncapitalized();
        final String setterName = "set" + field.getName().getCapitalized();

        JavaMethod setter = New.method(Methods.PUBLIC, accessorClass, setterName, New.parameter(field.getType(), "value"));

        setter.getBody().setHardcoded("this.%s.set(value);\nreturn this;",fieldName);
        return setter;
    }

    private JavaMethod makeGetter(JavaField field) {

        final String fieldName = field.getName().getUncapitalized();

        //Generate getter
        String getterPrefix = "get";
        if (field.getType().getSimpleName().getText().equalsIgnoreCase("boolean")) {
            getterPrefix = "is";
        }
        final String getterName = getterPrefix + field.getName().getCapitalized();
        final JavaMethod getter = New.method(Methods.PUBLIC,field.getType(), getterName);
        getter.getBody().setHardcoded("return this.%s.get();",fieldName);
        return getter;
    }

    private <T> T getAnnotationValue(Collection<JavaMetadata> metadatas, Class<? extends Annotation> annotation, Class<T> valueType) {
        for (JavaMetadata metadata : metadatas) {
            if (metadata.getAnnotation().getTypeClass().equals(annotation)) {
                //It's a canonical field
                return (T) metadata.getValues().get("value");
            } else {
                System.out.println(String.format("Types did not match: %s vs. %s",metadata.getAnnotation(),annotation));
            }
        }

        return null;
    }


    @Override
    public void process(PowerList<JavaClass> classes, ProcessingContext context) {

        JavaClassBean jAccesssor = (JavaClassBean) New.classs(Classes.PUBLIC_FINAL, "JAccesssor");
        jAccesssor.setParent(New.packagee("com.vonhof.jcanonical"));


        for (JavaClass original : classes) {
            JavaClass accessor = generateClass(original, context);
            if (accessor == null) {
                continue;
            }

            final JavaType targetClass = getTargetClass(original);

            JavaMethod forMethod = New.method(Methods.PUBLIC_STATIC, accessor.getType(), "a", New.parameter(targetClass, "delegate", true));
            forMethod.getBody().setHardcoded("return new %s(delegate);", accessor.getType().getSimpleName().getText());
            jAccesssor.getMethods().add(forMethod);

            context.getLogger().info("Generated class: {}",accessor.getName());
            context.generateCode(accessor, false);
        }


        context.generateCode(jAccesssor, false);
    }
}
