package evilcraft.api.config.configurable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import evilcraft.api.config.ElementTypeCategory;

/**
 * Property inside configs that can be configured in the config file.
 * @author rubensworks
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
public @interface ConfigurableProperty {
    /**
     * The category of the field.
     * @return The category.
     */
    ElementTypeCategory category();
    /**
     * The comment for the field in the config file.
     * @return The comment.
     */
    String comment();
    /**
     * Whether or not this field can be changed with commands at runtime.
     * @return If it is commandable.
     */
    boolean isCommandable() default false;
}
