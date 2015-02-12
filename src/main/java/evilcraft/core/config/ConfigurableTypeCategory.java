package evilcraft.core.config;

/**
 * An enumeration of {@link ConfigurableType} categories.
 * They will be categorised like this in the config file.
 * @author rubensworks
 *
 */
public enum ConfigurableTypeCategory {
    
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
     * Machine category.
     */
    MACHINE("machine"),
    /**
     * Potion category.
     */
    POTION("potion"),
    
    /**
     * Category with core settings.
     */
    CORE("core");
    
    /**
     * All the categories.
     */
    public static ConfigurableTypeCategory[] CATEGORIES = new ConfigurableTypeCategory[]{ITEM, BLOCK,
    	FLUID, ENTITY, GENERAL, WORLDGENERATION, ENCHANTMENT, MOB, BIOME,
    	DEGRADATIONEFFECT, MACHINE, POTION};
    
    private String name;
    
    private ConfigurableTypeCategory(String name) {
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
