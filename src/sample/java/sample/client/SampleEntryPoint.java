package com.haynesjm42.propertybindersample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point for {@code PropertyBinder} sample application
 *
 * @author John Haynes
 */
public class SampleEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {

        RootLayoutPanel.get().add(new HTML(
                "Super Dev Mode: " + MyProperties.INSTANCE.superDevMode() + "<br/>" +
                "testProp: " + MyProperties.INSTANCE.testProp() + "<br/>" +
                "testDouble: " + MyProperties.INSTANCE.testEnumeratedValues().toString()
        ));
    }
}
