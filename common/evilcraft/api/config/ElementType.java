package evilcraft.api.config;

/**
 * Types of elements
 * @author Ruben Taelman
 *
 */
public enum ElementType {
    ITEM(true),
    BLOCK(true),
    MOB(false),
    ENTITY(false),
    LIQUID(true);
    
    private boolean uniqueInstance = false;
    
    private ElementType(boolean uniqueInstance) {
        this.uniqueInstance = uniqueInstance;
    }
    
    public boolean hasUniqueInstance() {
        return uniqueInstance;
    }
}
