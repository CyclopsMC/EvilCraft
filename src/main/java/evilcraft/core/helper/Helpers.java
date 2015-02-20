package evilcraft.core.helper;

import evilcraft.core.config.configurable.IConfigurable;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of helper methods and fields.
 * @author rubensworks
 *
 */
public class Helpers {
    
    private static Map<IDType, Integer> ID_COUNTER = new HashMap<IDType, Integer>();
    
    /**
     * Safe parsing of a string to it's real object type.
     * The real object type is determined by checking the class of the oldValue.
     * @param newValue The value to parse
     * @param oldValue The old value that has a certain type.
     * @return The parsed newValue.
     */
    public static Object tryParse(String newValue, Object oldValue) {
        Object newValueParsed = null;
        try {
            if(oldValue instanceof Integer) {
                newValueParsed = Integer.parseInt(newValue);
            } else if(oldValue instanceof Boolean) {
                newValueParsed = Boolean.parseBoolean(newValue);
            } else if(oldValue instanceof Double) {
                newValueParsed = Double.parseDouble(newValue);
            } else if(oldValue instanceof String) {
                newValueParsed = newValue;
            }
        } catch (Exception e) {}
        return newValueParsed;
    }
    
    /**
     * Get a new ID for the given type.
     * @param type Type for a {@link IConfigurable}.
     * @return The incremented ID.
     */
    public static int getNewId(IDType type) {
    	Integer ID = ID_COUNTER.get(type);
    	if(ID == null) ID = 0;
    	ID_COUNTER.put(type, ID + 1);
    	return ID;
    }
    
    /**
     * Type of ID's to use in {@link Helpers#getNewId(IDType)}
     * @author rubensworks
     *
     */
    public enum IDType {
    	/**
    	 * Entity ID.
    	 */
    	ENTITY,
    	/**
    	 * GUI ID.
    	 */
    	GUI,
    	/**
    	 * Packet ID.
    	 */
    	PACKET;
    }
}
