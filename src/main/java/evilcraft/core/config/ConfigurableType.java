package evilcraft.core.config;

import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.configurabletypeaction.*;
import evilcraft.core.config.extendedconfig.*;

/**
 * The different types of {@link IConfigurable}.
 * @author rubensworks
 *
 */
public class ConfigurableType {
    
    /**
     * Item type.
     */
    public static final ConfigurableType ITEM = new ConfigurableType(true, ItemConfig.class, new ItemAction(), ConfigurableTypeCategory.ITEM);
    /**
     * Block type.
     */
    public static final ConfigurableType BLOCK = new ConfigurableType(true, BlockConfig.class, new BlockAction(), ConfigurableTypeCategory.BLOCK);
    /**
     * Block type with containers.
     */
    public static final ConfigurableType BLOCKCONTAINER = new ConfigurableType(true, BlockConfig.class, new BlockAction(), ConfigurableTypeCategory.BLOCK);
    /**
     * Mob type.
     */
    public static final ConfigurableType MOB = new ConfigurableType(false, MobConfig.class, new MobAction(), ConfigurableTypeCategory.MOB);
    /**
     * Regular entity type.
     */
    public static final ConfigurableType ENTITY = new ConfigurableType(false, EntityConfig.class, new EntityAction(), ConfigurableTypeCategory.ENTITY);
    /**
     * Fluid type.
     */
    public static final ConfigurableType FLUID = new ConfigurableType(true, FluidConfig.class, new FluidAction(), ConfigurableTypeCategory.FLUID);
    /**
     * Enchantment type.
     */
    public static final ConfigurableType ENCHANTMENT = new ConfigurableType(true, EnchantmentConfig.class, new EnchantmentAction(), ConfigurableTypeCategory.ENCHANTMENT);
    /**
     * Villager type.
     */
    public static final ConfigurableType VILLAGER = new ConfigurableType(true, VillagerConfig.class, new VillagerAction(), ConfigurableTypeCategory.MOB);
    /**
     * Biome type.
     */
    public static final ConfigurableType BIOME = new ConfigurableType(true, BiomeConfig.class, new BiomeAction(), ConfigurableTypeCategory.BIOME);
    /**
     * Degradation effect type.
     */
    public static final ConfigurableType DEGRADATIONEFFECT = new ConfigurableType(true, DegradationEffectConfig.class, new DegradationEffectAction(), ConfigurableTypeCategory.DEGRADATIONEFFECT);
    /**
     * Potion effect type.
     */
    public static final ConfigurableType POTION = new ConfigurableType(true, PotionConfig.class, new PotionAction(), ConfigurableTypeCategory.POTION);


    /**
     * Dummy type, only used for configs that refer to nothing.
     */
    public static final ConfigurableType DUMMY = new ConfigurableType(false, DummyConfig.class, new DummyAction(), ConfigurableTypeCategory.GENERAL);
    
    private boolean uniqueInstance = false;
    @SuppressWarnings("rawtypes")
    private Class<? extends ExtendedConfig> configClass;
    @SuppressWarnings("rawtypes")
    private ConfigurableTypeAction action;
    private ConfigurableTypeCategory category = null;
    private String categoryRaw;
    
    /**
     * Make a new instance.
     * @param uniqueInstance If this type has a unique instance for each config.
     * @param configClass The config class.
     * @param action The action instance that helps with saving of the config and optional instance.
     * @param category The category in which the configs should be saved.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableType(boolean uniqueInstance, Class<? extends ExtendedConfig> configClass,
            ConfigurableTypeAction action, ConfigurableTypeCategory category) {
        this.uniqueInstance = uniqueInstance;
        this.configClass = configClass;
        this.action = action;
        this.category = category;
    }
    
    /**
     * Make a new instance with a raw category.
     * @param uniqueInstance If this type has a unique instance for each config.
     * @param configClass The config class.
     * @param action The action instance that helps with saving of the config and optional instance.
     * @param category The category in which the configs should be saved.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableType(boolean uniqueInstance, Class<? extends ExtendedConfig> configClass,
            ConfigurableTypeAction action, String category) {
        this.uniqueInstance = uniqueInstance;
        this.configClass = configClass;
        this.action = action;
        this.categoryRaw = category;
    }
    
    /**
     * If this type should refer to a {@link IConfigurable} with a unique instance.
     * If this is true, the {@link IConfigurable} should have a public static void
     * initInstance(ExtendedConfig eConfig) method and also a public static
     * (? extends IConfigurable) getInstance() method.
     * @return If it has a unique instance.
     */
    public boolean hasUniqueInstance() {
        return uniqueInstance;
    }
    
    /**
     * Get the class that extends {@link ExtendedConfig} this type can hold.
     * @return The class that extends {@link ExtendedConfig} this type can hold.
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends ExtendedConfig> getConfigClass() {
        return configClass;
    }
    
    /**
     * The action for this type after the the {@link IConfigurable} has configured so it can be registered.
     * @return The action for this type.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableTypeAction getElementTypeAction() {
        return action;
    }
    
    /**
     * The category of this type.
     * @return The category.
     */
    public String getCategory() {
    	if(category == null) {
    		return categoryRaw;
    	}
        return category.toString();
    }
}
