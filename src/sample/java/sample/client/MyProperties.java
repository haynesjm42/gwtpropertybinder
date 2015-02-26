package com.haynesjm42.propertybindersample.client;

import com.google.gwt.core.client.GWT;
import com.haynesjm42.propertybinder.client.PropertyBinder;

/**
 * Sample usage of {@link PropertyBinder}
 *
 * @author John Haynes
 */
public interface MyProperties extends PropertyBinder {
    public static MyProperties INSTANCE = GWT.create(MyProperties.class);

    @PropertyName("superdevmode")
    @TrueWhen(value = "on")
    boolean superDevMode();

    String testProp();

    public enum EnumeratedValues {
        Value1,
        Value2,
        Value3
    }

    @PropertyName("enumeratedValues")
    EnumeratedValues testEnumeratedValues();
}
