package evilcraft.api.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import evilcraft.EvilCraft;

/**
 * Registration configurations
 * @author Ruben Taelman
 *
 */
public abstract class ExtendedConfig implements Comparable<ExtendedConfig>{
    
    public int ID;
    public String NAME;
    public String NAMEDID;
    public String COMMENT;
    public Class ELEMENT;
    
    // To store additional stuff inside the config
    public List<ConfigProperty> configProperties = new LinkedList<ConfigProperty>();
    
    /**
     * Create a new config
     * @param defaultId the id for this element (preferably from a config file)
     * @param name the name to be displayed
     * @param namedId a unique name id
     * @param comment a comment that can be added to the config file line
     * @param element the class for the element this config is for
     */
    public ExtendedConfig(int defaultId, String name, String namedId, String comment, Class element) {
        this.ID = defaultId;
        this.NAME = name;
        this.NAMEDID = namedId;
        this.COMMENT = comment;
        this.ELEMENT = element;
        try {
            generateConfigProperties();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Generate the list of ConfigProperties by checking all the fields with the ConfigurableProperty
     * annotation.
     * 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void generateConfigProperties() throws IllegalArgumentException, IllegalAccessException {
        for(final Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ConfigurableProperty.class)) {
                
                ConfigProperty configProperty = new ConfigProperty(
                        field.getAnnotation(ConfigurableProperty.class).category(),
                        this.NAMEDID + "." + field.getName(),
                        field.getInt(null),
                        field.getAnnotation(ConfigurableProperty.class).comment(),
                        new ConfigPropertyCallback() {
                            @Override
                            public void run(int newValue) {
                                try {
                                    field.set(null, newValue);
                                } catch (IllegalArgumentException
                                        | IllegalAccessException e) {
                                    // Shouldn't be possible
                                    e.printStackTrace();
                                }
                            }
                        });
                configProperties.add(configProperty);
            }
        }
    }
    
    /**
     * Save this config inside the correct element and inside the implementation if itself
     * @throws Throwable 
     */
    public void save() {
        try {
            // Save inside the self-implementation
            this.getClass().getField("_instance").set(null, this);
            
            // Save inside the unique instance this config refers to (only if such an instance exists!)
            if(this.getHolderType().hasUniqueInstance())
                this.ELEMENT.getMethod("initInstance", ExtendedConfig.class).invoke(null, this);
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException
                | NoSuchMethodException
                | SecurityException | NoSuchFieldException e) {
            // Only possible in development mode
            e.printStackTrace();
        }
    }
    
    /**
     * Return the type for which this object holds data
     * @return the elementType of the object to where the config belongs
     */
    public ElementType getHolderType() {
        try {
            return (ElementType) this.ELEMENT.getField("TYPE").get(null);
        } catch (IllegalArgumentException | IllegalAccessException
                | NoSuchFieldException | SecurityException e) {
            // Only possible in development mode
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Will return the instance of the object this config refers to
     * @return instance of sub object
     */
    public Configurable getSubInstance() {
        if(!this.getHolderType().hasUniqueInstance()) return null; // TODO: possibly add a nice exception here
        try {
            return (Configurable) this.ELEMENT.getMethod("getInstance").invoke(null);
        } catch (NoSuchMethodException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            // Only possible in development mode
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Will return the unique name of the object this config refers to
     * @return unique name of sub object
     */
    public String getSubUniqueName() {
        return NAMEDID;
    }
    
    /**
     * Overridable method that is called after the element of this config is registered.
     */
    public void onRegistered() {
        
    }
    
    @Override
    public int compareTo(ExtendedConfig o) {
        return NAMEDID.compareTo(o.NAMEDID);
    }
    
    /**
     * A holder class for properties that go inside the config file.
     *
     */
    public class ConfigProperty {
        private String category;
        private String name;
        private int value;
        private String comment;
        private ConfigPropertyCallback callback;
        
        public ConfigProperty(String category, String name, int value, String comment, ConfigPropertyCallback callback) {
            this.category = category;
            this.name = name;
            this.value = value;
            this.comment = comment;
            this.callback = callback;
        }
        
        public ConfigProperty(String category, String name, int value, ConfigPropertyCallback callback) {
            this(category, name, value, null, callback);
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
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
    }
    
    public abstract class ConfigPropertyCallback {
        public abstract void run(int newValue);
    }
}
