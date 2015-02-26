package com.haynesjm42.propertybinder.client;

import java.lang.annotation.*;

/**
 * Base interface whose sub-types will be generated by the GWT compiler, giving access to properties from the GWT module
 * XML files.<br/>
 * Extenders of this interface must use <code>GWT.create()</code> to create the instances of their property binder.
 * <pre>
 * public interface MyProperties extends PropertyBinder {
 *  public static MyProperties INSTANCE = GWT.create(MyProperties.class);
 *
 *  {@literal @}PropertyName("superdevmode")
 *  {@literal @}TrueWhen(value = "on")
 *  boolean isSuperDevMode();
 * }
 * </pre>
 *
 * @author John Haynes
 */
public interface PropertyBinder {
    /**
     * Specifies the property name to bind to when the method name is not equal to the property name.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PropertyName {
        String value();
    }

    /**
     * Specifies the value that represents <code>true</code> for boolean getter methods.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface TrueWhen {
        String value();
    }
}
