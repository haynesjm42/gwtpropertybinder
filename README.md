gwtpropertybinder
=================
The idea behind PropertyBinder is to make it easy to access properties from your module XML files by extending an interface and using annotations as needed, without requiring a large amount of boilerplate and deferred binding rules. A good use-case for this would be to determine if you are running in superdevmode at runtime, by having PropertyBinder generate a getter for the `superdevmode` property, like so:
<br><br>
```java
public interface MyProperties extends PropertyBinder {
  /**
   * Getter for the superdevmode property.
   */
  @PropertyName("superdevmode")
  @TrueWhen(value = "on")
  boolean isSuperDevMode();
}
```
