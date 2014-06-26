package evilcraft.api.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Level;

import evilcraft.EvilCraft;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.api.config.configurable.ConfigurableProperty;

/**
 * A config that refers to a {@link Configurable}. Every unique {@link Configurable} must have one
 * unique extension of this class. This contains several configurable settings and properties
 * that can also be set in the config file.
 * @author rubensworks
 * @param <C> Class of the extension of ExtendedConfig
 *
 */
public abstract class ExtendedConfig<C extends ExtendedConfig<C>> implements Comparable<ExtendedConfig<C>>{
    
	private boolean enabled;

	/**
     * The unique name ID for the configurable.
     */
    public String NAMEDID;
    /**
     * The comment to add in the config file for this configurable.
     */
    public String COMMENT;
    /**
     * The class of this configurable.
     */
    @SuppressWarnings("rawtypes")
    public Class ELEMENT;
    
    /**
     * A list of {@link ConfigProperty} that can contain additional settings for this configurable.
     */
    public List<ConfigProperty> configProperties = new LinkedList<ConfigProperty>();
    
    /**
     * Create a new config
     * @param enabled If this should is enabled by default. If this is false, this can still
     * be enabled through the config file.
     * @param namedId a unique name id
     * @param comment a comment that can be added to the config file line
     * @param element the class for the element this config is for
     */
    public ExtendedConfig(boolean enabled, String namedId, String comment, Class<?> element) {
    	this.enabled = enabled;
        this.NAMEDID = namedId;
        this.COMMENT = comment;
        this.ELEMENT = element;
        try {
            generateConfigProperties();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
        	e2.printStackTrace();
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
                        field.get(null),
                        field.getAnnotation(ConfigurableProperty.class).comment(),
                        new ConfigPropertyCallback() {
                            @Override
                            public void run(Object newValue) {
                                try {
                                    field.set(null, newValue);
                                } catch (IllegalArgumentException e1) {
                                    // Shouldn't be possible
                                    e1.printStackTrace();
                                } catch (IllegalAccessException e2) {
                                	e2.printStackTrace();
                                }
                            }
                        },
                        field.getAnnotation(ConfigurableProperty.class).isCommandable(),
                        field);
                configProperties.add(configProperty);
            }
        }
    }
    
    /**
     * Save this config inside the correct element and inside the implementation if itself.
     */
    @SuppressWarnings("unchecked")
    public void save() {
        String errorMessage = "Registering " + this.NAMEDID + " caused an issue.";
        try {
            // Save inside the self-implementation
            this.getClass().getField("_instance").set(null, this);
            
            // Save inside the unique instance this config refers to (only if such an instance exists!)
            if(this.getHolderType().hasUniqueInstance())
                this.ELEMENT.getMethod("initInstance", ExtendedConfig.class).invoke(null, this);
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
            throw new EvilCraftConfigException("Registering " + this.NAMEDID + " caused the issue: " + e.getCause().getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new EvilCraftConfigException(errorMessage);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new EvilCraftConfigException(errorMessage);
        } catch (NoSuchMethodException e) {
        	e.printStackTrace();
        	throw new EvilCraftConfigException(errorMessage);
        } catch (SecurityException e) {
        	e.printStackTrace();
        	throw new EvilCraftConfigException(errorMessage); 
        } catch (NoSuchFieldException e) {
        	e.printStackTrace();
        	throw new EvilCraftConfigException(errorMessage);
        }
    }
    
    /**
     * Return the type for which this object holds data
     * @return the elementType of the object to where the config belongs
     */
    public ElementType getHolderType() {
        try {
            return (ElementType) this.ELEMENT.getField("TYPE").get(null);
        } catch (IllegalArgumentException e1) {
            // Only possible in development mode
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
        	e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
        	e3.printStackTrace();
        } catch (SecurityException e4) {
        	e4.printStackTrace();
        }
        return null;
    }
    
    /**
     * Will return the instance of the object this config refers to
     * @return instance of sub object
     */
    @SuppressWarnings("unchecked")
    public Configurable getSubInstance() {
        if(!this.getHolderType().hasUniqueInstance())
            throw new EvilCraftConfigException("There exists no unique instance for " + this);
        try {
            return (Configurable) this.ELEMENT.getMethod("getInstance").invoke(null);
        } catch (NoSuchMethodException e1) {
        	// Only possible in development mode
        	e1.printStackTrace();
        } catch (SecurityException e2) {
        	e2.printStackTrace();
        } catch (IllegalAccessException e3) {
        	e3.printStackTrace();
        } catch (IllegalArgumentException e4) {
        	e4.printStackTrace();
        } catch (InvocationTargetException e5) {
        	e5.printStackTrace();
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
    public int compareTo(ExtendedConfig<C> o) {
        return NAMEDID.compareTo(o.NAMEDID);
    }
    
    /**
     * Checks if the eConfig refers to a target that should be enabled.
     * @return if the target should be enabled.
     */
    public boolean isEnabled() {
        return this.enabled && !isHardDisabled();
    }
    
    /**
     * Set the enabling of the target.
     * @param enabled If the target should be enabled.
     */
    public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
    
    /**
     * If the target should be hard-disabled, this means no occurence in the config file,
     * total ignorance.
     * @return if the target should run trough the config handler.
     */
    public boolean isHardDisabled() {
        return false;
    }
    
    /**
     * Override this method to prevent configs to be disabled from the config file. (non-zero id's that is)
     * @return if the target can be disabled.
     */
    public boolean isDisableable() {
        return true;
    }
    
    /**
     * Call this method in the initInstance method of Configurables if the instance was already set.
     */
    public void showDoubleInitError() {
        String message = this.getClass()+" caused a double registration of "+getSubInstance()+". This is an error in the mod code.";
        EvilCraft.log(message, Level.FATAL);
        throw new EvilCraftConfigException(message);
    }
    
    /**
     * Get the lowest castable config.
     * @return The downcasted config.
     */
    @SuppressWarnings("unchecked")
    public C downCast() {
        C c = (C) this;
        return c;
    }
}
