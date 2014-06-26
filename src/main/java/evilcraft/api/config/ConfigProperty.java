package evilcraft.api.config;

import java.lang.reflect.Field;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.logging.log4j.Level;

import evilcraft.EvilCraft;
import evilcraft.api.config.configurable.Configurable;

/**
 * A holder class for properties that go inside the config file.
 * Used inside the {@link ConfigHandler} for configuring the settings of the {@link Configurable}.
 * @author rubensworks
 *
 */
public class ConfigProperty {
    
    private ElementTypeCategory category;
    private String name;
    private Object value;
    private String comment;
    private ConfigPropertyCallback callback;
    private boolean isCommandable;
    private Field field;
    
    /**
     * Define a new configurable property.
     * @param category Category.
     * @param name Name of the property.
     * @param value Value of the property.
     * @param comment Comment of the property for in the config file.
     * @param callback Callback object for when this property is configured.
     * @param isCommandable If this property should be able to be changed at runtime via commands.
     * @param field The field of the {@link ExtendedConfig} this property refers to.
     */
    public ConfigProperty(ElementTypeCategory category, String name, Object value, String comment, ConfigPropertyCallback callback, boolean isCommandable, Field field) {
        this.category = category;
        this.name = name;
        this.value = value;
        this.comment = comment;
        this.callback = callback;
        this.isCommandable = isCommandable;
        this.field = field;
    }
    
    /**
     * Define a new configurable property without a comment.
     * @param category Category.
     * @param name Name of the property.
     * @param value Value of the property.
     * @param callback Callback object for when this property is configured.
     * @param isCommandable If this property should be able to be changed at runtime via commands.
     * @param field The field of the {@link ExtendedConfig} this property refers to.
     */
    public ConfigProperty(ElementTypeCategory category, String name, Object value, ConfigPropertyCallback callback, boolean isCommandable, Field field) {
        this(category, name, value, null, callback, isCommandable, field);
    }

    /**
     * Get the category.
     * @return The category.
     */
    public ElementTypeCategory getCategory() {
        return category;
    }

    /**
     * Set the category.
     * @param category The category to set.
     */
    public void setCategory(ElementTypeCategory category) {
        this.category = category;
    }

    /**
     * Get the name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     * @param name The name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value.
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value.
     * @param value The value to be set.
     */
    public void setValue(Object value) {
        this.value = value;
        try {
            field.set(null, this.value);
        } catch (IllegalArgumentException e1) {
            // Won't happen, trust me.
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * Get the comment.
     * @return The comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment.
     * @param comment The comment to be set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the callback.
     * @return The callback.
     */
    public ConfigPropertyCallback getCallback() {
        return callback;
    }

    /**
     * Set the calback.
     * @param callback The callback to be set.
     */
    public void setCallback(ConfigPropertyCallback callback) {
        this.callback = callback;
    }
    
    /**
     * If this property can be configured with commands.
     * @return Is this commandable.
     */
    public boolean isCommandable() {
        return isCommandable;
    }

    /**
     * Set if this property can be configured with commands.
     * @param isCommandable True if this property can be configured with commands.
     */
    public void setCommandable(boolean isCommandable) {
        this.isCommandable = isCommandable;
    }
    
    /**
     * Save this property in the given config file.
     * @param config The config file to save to.
     */
    public void save(Configuration config) {
        save(config, false);
    }
    
    /**
     * Save this property in the given config file.
     * @param config The config file to save to.
     * @param forceUpdate If the value in the config has to be overwritten.
     */
    public void save(Configuration config, boolean forceUpdate) {
        // Sorry, no cleaner solution for this...
        // Reflection could solve it partially, but it'd be still quite ugly...
        String category = getCategory().toString();
        String name = getName();
        Object value = getValue();
        
        Property additionalProperty = null;
        if(value instanceof Integer) {
            additionalProperty = config.get(
                category,
                name,
                (Integer)value
                );
            additionalProperty.comment = getComment();
            if(forceUpdate) {
                getCallback().run((Integer)value);
            } else {
                getCallback().run(additionalProperty.getInt());
            }
        } else if(value instanceof Boolean) {
            additionalProperty = config.get(
                category,
                name,
                (Boolean)value
                );
            additionalProperty.comment = getComment();
            if(forceUpdate) {
                getCallback().run((Boolean)value);
            } else {
                getCallback().run(additionalProperty.getBoolean((Boolean)value));
            }
            
        } else if(value instanceof Double) {
            additionalProperty = config.get(
                    category,
                    name,
                    (Double)value
                    );
                additionalProperty.comment = getComment();
                if(forceUpdate) {
                    getCallback().run((Double)value);
                } else {
                    getCallback().run(additionalProperty.getDouble((Double)value));
                } 
        } else if(value instanceof String) {
            additionalProperty = config.get(
                category,
                name,
                (String)value
                );
            additionalProperty.comment = getComment();
            if(forceUpdate) {
                getCallback().run((String)value);
            } else {
                getCallback().run(additionalProperty.getString());
            }
        } else {
            EvilCraft.log("Invalid config property class. No match found for '"
            		+ name + "': '" + value + "'", Level.ERROR);
        }
    }
}