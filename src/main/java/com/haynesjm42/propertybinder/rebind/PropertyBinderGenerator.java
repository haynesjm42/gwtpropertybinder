package com.haynesjm42.propertybinder.rebind;

import com.google.gwt.core.ext.*;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.haynesjm42.propertybinder.client.PropertyBinder;

import java.io.PrintWriter;

/**
 * Generator for {@link PropertyBinder}
 *
 * @author John Haynes
 */
public class PropertyBinderGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        try {
            JClassType type = context.getTypeOracle().getType(typeName);
            SourceWriter writer = createSourceWriter(logger, context, type);

            if (writer != null) {
                for (JMethod method : type.getMethods()) {
                    String propertyName;
                    PropertyBinder.PropertyName propertyNameAnnotation = method.getAnnotation(PropertyBinder.PropertyName.class);
                    if (propertyNameAnnotation != null) {
                        propertyName = propertyNameAnnotation.value();
                    } else {
                        propertyName = method.getName();
                    }

                    SelectionProperty property;
                    try {
                        property = context.getPropertyOracle().getSelectionProperty(logger, propertyName);
                    } catch (BadPropertyValueException e) {
                        logger.log(TreeLogger.Type.ERROR, "Unable to retrieve value for property '" + propertyName + "'");
                        throw new UnableToCompleteException();
                    }

                    String returnTypeName;
                    if (method.getReturnType().isPrimitive() != null) {
                        returnTypeName = method.getReturnType().isPrimitive().getQualifiedBoxedSourceName();
                    } else {
                        returnTypeName = method.getReturnType().getQualifiedSourceName();
                    }

                    String returnValue;
                    if (returnTypeName.equals(Boolean.class.getName())) {
                        // If we are generating a boolean getter, then check if they provided a value that denotes "true"
                        // If not then just let Boolean.valueOf() take care of the conversion
                        PropertyBinder.TrueWhen trueWhenAnnotation = method.getAnnotation(PropertyBinder.TrueWhen.class);
                        boolean value;
                        if (trueWhenAnnotation != null) {
                            value = property.getCurrentValue().equals(trueWhenAnnotation.value());
                        } else {
                            value = Boolean.valueOf(property.getCurrentValue());
                        }
                        returnValue = Boolean.toString(value);
                    } else if (returnTypeName.equals(String.class.getName())) {
                        // For string getters, just return the property value
                        returnValue = "\"" + property.getCurrentValue() + "\"";
                    } else {
                        // If the return type is not a String or Boolean, then check if the type is a class and has
                        // a static valueOf(String) method that can take care of the conversion
                        JClassType returnClassType = method.getReturnType().isClass();
                        JType[] paramTypes = {context.getTypeOracle().getType(String.class.getName())};
                        if (returnClassType != null) {
                            JMethod valueOfMethod = returnClassType.getMethod("valueOf", paramTypes);
                            if (valueOfMethod != null && valueOfMethod.isStatic()) {
                                returnValue = returnTypeName + ".valueOf(\"" + property.getCurrentValue() + "\")";
                            } else {
                                throw new IllegalArgumentException(returnTypeName + " does not have a static valueOf(String) method");
                            }
                        } else {
                            throw new IllegalArgumentException(returnTypeName + " is not a bindable property value type");
                        }
                    }

                    // Write out the getter
                    writer.println("public " + method.getReturnType().getSimpleSourceName() + " " + method.getName() + "() { ");
                    writer.indentln("return " + returnValue + ";");
                    writer.println("}");
                    writer.println();
                }

                writer.commit(logger);
            }

            return type.getQualifiedSourceName() + "Impl";

        } catch (NotFoundException e) {
            logger.log(TreeLogger.Type.ERROR, "Could not find type '" + typeName + "'");
        }

        throw new UnableToCompleteException();
    }

    private SourceWriter createSourceWriter(TreeLogger logger, GeneratorContext context, JClassType type) {
        String simpleName = type.getSimpleSourceName() + "Impl";
        String packageName = type.getPackage().getName();

        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
        composer.addImplementedInterface(type.getName());

        PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
        return (printWriter != null) ? composer.createSourceWriter(context, printWriter) : null;
    }
}
