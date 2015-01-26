package evilcraft.core.config.extendedconfig;

import evilcraft.EvilCraft;
import evilcraft.IInitListener;
import evilcraft.core.config.*;
import evilcraft.core.config.configurable.IConfigurable;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * A config that refers to a {@link IConfigurable}. Every unique {@link IConfigurable} must have one
 * unique extension of this class. This contains several configurable settings and properties
 * that can also be set in the config file.
 * @author rubensworks
 * @param <C> Class of the extension of ExtendedConfig
 *
 */
public abstract class ExtendedConfig<C extends ExtendedConfig<C>> implements
	Comparable<ExtendedConfig<C>>, IInitListener{
    
	private boolean enabled;

    private String namedId;
    private String comment;
    @SuppressWarnings("rawtypes")
	private Class element;

    private IConfigurable overriddenSubInstance;
    
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
    	this.namedId = namedId;
    	this.comment = comment;
    	this.element = element;
        try {
            generateConfigProperties();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
        	e2.printStackTrace();
        }
    }
    
    /**
     * @return The unique name ID for the configurable.
     */
    public String getNamedId() {
		return namedId;
	}

    /**
     * @return The comment to add in the config file for this configurable.
     */
	public String getComment() {
		return comment;
	}

	/**
	 * @return The class of this configurable.
	 */
	@SuppressWarnings("rawtypes")
	public Class getElement() {
		return element;
	}

	/**
     * Generate the list of ConfigProperties by checking all the fields with the ConfigurableProperty
     * annotation.
     * 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void generateConfigProperties() throws IllegalArgumentException, IllegalAccessException {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ConfigurableProperty.class)) {
            	ConfigurableProperty annotation = field.getAnnotation(ConfigurableProperty.class);
            	IChangedCallback changedCallback = null;
            	if(annotation.changedCallback() != IChangedCallback.class) {
            		try {
						changedCallback = annotation.changedCallback().newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
            	}
            	String category = annotation.categoryRaw().equals("") ? annotation.category().toString() : annotation.categoryRaw();
                ConfigProperty configProperty = new ConfigProperty(
                		category,
                        getConfigPropertyPrefix() + "." + field.getName(),
                        field.get(null),
                        annotation.comment(),
                        new ConfigPropertyCallback(changedCallback),
                        annotation.isCommandable(),
                        field);
                configProperty.setRequiresWorldRestart(annotation.requiresWorldRestart());
                configProperty.setRequiresMcRestart(annotation.requiresMcRestart());
                configProperties.add(configProperty);
            }
        }
    }
    
    /**
     * @return The prefix that will be used inside the config file for {@link ConfigurableProperty}'s.
     */
    protected String getConfigPropertyPrefix() {
		return this.getNamedId();
	}

	/**
     * Save this config inside the correct element and inside the implementation if itself.
     */
    @SuppressWarnings("unchecked")
    public void save() {
        String errorMessage = "Registering " + this.getNamedId() + " caused an issue.";
        try {
            // Save inside the self-implementation
            this.getClass().getField("_instance").set(null, this);

            // Try initalizing the override sub instance.
            this.overriddenSubInstance = initSubInstance();

            // Save inside the unique instance this config refers to (only if such an instance exists!)
            if (getOverriddenSubInstance() == null && this.getHolderType().hasUniqueInstance()) {
                this.getElement().getMethod("initInstance", ExtendedConfig.class).invoke(null, this);
            }
        } catch (InvocationTargetException e) {
            EvilCraft.log("Registering " + this.getNamedId() + " caused the issue "
                    + "(skipping registration): " + e.getCause().getMessage(), Level.ERROR);
            e.getCause().printStackTrace();

            // Disable this configurable.
            if (!this.isDisableable()) {
                throw new EvilCraftConfigException("Registering " + this.getNamedId()
                        + " caused the issue: " + e.getCause().getMessage()
                        + ". Since this is a required element of this mod, we can not continue, "
                        + "there might be ID conflicts with other mods.");
            }
            this.setEnabled(false);
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
     * Return the configurable type for which this config holds data
     * @return the type of the configurable to where the config belongs
     */
    public abstract ConfigurableType getHolderType();
    
    /**
     * Get the unlocalized name (must be unique!) for this configurable.
     * @return The unlocalized name.
     */
    public abstract String getUnlocalizedName();

    /**
     * Get the full unlocalized name for this configurable.
     * @return The unlocalized name.
     */
    public String getFullUnlocalizedName() {
        return getUnlocalizedName();
    }

    /**
     * This method will by default just return null.
     * If it returns something else, this config will assume that the object that is returned is the unique sub-instance
     * for the configurable.
     * This is only called once.
     * @return A sub-instance that will become a singleton.
     */
    protected IConfigurable initSubInstance() {
        return null;
    }

    private IConfigurable getOverriddenSubInstance() {
        return this.overriddenSubInstance;
    }

    /**
     * Will return the instance of the object this config refers to
     * @return instance of sub object
     */
    @SuppressWarnings("unchecked")
    public IConfigurable getSubInstance() {
        if(getOverriddenSubInstance() != null) {
            return getOverriddenSubInstance();
        }
        if(!this.getHolderType().hasUniqueInstance())
            throw new EvilCraftConfigException("There exists no unique instance for " + this);
        try {
            return (IConfigurable) this.getElement().getMethod("getInstance").invoke(null);
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
        return getNamedId();
    }
    
    /**
     * Overridable method that is immediately called after the element of this config is registered.
     */
    public void onRegistered() {
        
    }
    
    @Override
    public void onInit(IInitListener.Step step) {
    	
    }
    
    @Override
    public int compareTo(ExtendedConfig<C> o) {
        return getNamedId().compareTo(o.getNamedId());
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
