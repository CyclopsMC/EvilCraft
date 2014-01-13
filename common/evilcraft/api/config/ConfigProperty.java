package evilcraft.api.config;

import java.lang.reflect.Field;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig.ConfigPropertyCallback;

/**
 * A holder class for properties that go inside the config file.
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
    
    public ConfigProperty(ElementTypeCategory category, String name, Object value, String comment, ConfigPropertyCallback callback, boolean isCommandable, Field field) {
        this.category = category;
        this.name = name;
        this.value = value;
        this.comment = comment;
        this.callback = callback;
        this.isCommandable = isCommandable;
        this.field = field;
    }
    
    public ConfigProperty(ElementTypeCategory category, String name, Object value, ConfigPropertyCallback callback, boolean isCommandable, Field field) {
        this(category, name, value, null, callback, isCommandable, field);
    }

    public ElementTypeCategory getCategory() {
        return category;
    }

    public void setCategory(ElementTypeCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ConfigPropertyCallback getCallback() {
        return callback;
    }

    public void setCallback(ConfigPropertyCallback callback) {
        this.callback = callback;
    }
    
    public boolean isCommandable() {
        return isCommandable;
    }

    public void setCommandable(boolean isCommandable) {
        this.isCommandable = isCommandable;
    }
    
    public void save(Configuration config) {
        save(config, false);
    }
    
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
            EvilCraft.log("Invalid config property class.", Level.SEVERE);
        }
    }
}