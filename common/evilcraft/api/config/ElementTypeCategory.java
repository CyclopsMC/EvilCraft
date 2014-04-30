package evilcraft.api.config;

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
    
    private String name;
    
    private ElementTypeCategory(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
