package evilcraft.core.config;

/**
 * An enumeration of {@link ElementType} categories.
 * They will be categorised like this in the config file.
 * @author rubensworks
 *
 */
public enum ElementTypeCategory {
    
    /**
     * Item category.
     */
    ITEM("item"),
    /**
     * Block category.
     */
    BLOCK("block"),
    /**
     * Fluid category.
     */
    FLUID("fluid"),
    /**
     * Entity category.
     */
    ENTITY("entity"),
    /**
     * General category.
     */
    GENERAL("general"),
    /**
     * Ore generation category.
     */
    OREGENERATION("oregeneration"),
    /**
     * World generation category.
     */
    WORLDGENERATION("worldgeneration"),
    /**
     * Enchantment category.
     */
    ENCHANTMENT("enchantment"),
    /**
     * Mob category.
     */
    MOB("mob"),
    /**
     * Biome category.
     */
    BIOME("biome"),
    /**
     * Degradation Effect category.
     */
    DEGRADATIONEFFECT("degradation effect"),
    
    /**
     * Category with core settings.
     */
    CORE("core");
    
    /**
     * All the categories.
     */
    public static ElementTypeCategory[] CATEGORIES = new ElementTypeCategory[]{ITEM, BLOCK,
    	FLUID, ENTITY, GENERAL, OREGENERATION, WORLDGENERATION, ENCHANTMENT, MOB, BIOME,
    	DEGRADATIONEFFECT};
    
    private String name;
    
    private ElementTypeCategory(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Get the original to string.
     * @return The enum name.
     */
    public String rawToString() {
        return super.toString();
    }
    
}
