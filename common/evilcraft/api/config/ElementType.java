package evilcraft.api.config;

/**
 * Types of elements
 * @author Ruben Taelman
 *
 */
public enum ElementType {
    ITEM(true),
    BLOCK(true),
    BLOCKCONTAINER(true),
    MOB(false),
    ENTITY(false),
    LIQUID(true),
    ENCHANTMENT(true),
    VILLAGER(false),
    ENTITY_NO_GLOBAL_ID(false),
    
    DUMMY(false);
    
    private boolean uniqueInstance = false;
    
    private ElementType(boolean uniqueInstance) {
        this.uniqueInstance = uniqueInstance;
    }
    
    public boolean hasUniqueInstance() {
        return uniqueInstance;
    }
}
