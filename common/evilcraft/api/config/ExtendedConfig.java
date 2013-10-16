package evilcraft.api.config;

import java.lang.reflect.InvocationTargetException;

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
}
