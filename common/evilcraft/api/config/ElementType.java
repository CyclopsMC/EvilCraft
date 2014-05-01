package evilcraft.api.config;

import evilcraft.api.config.configurable.Configurable;
import evilcraft.api.config.elementtypeaction.BiomeAction;
import evilcraft.api.config.elementtypeaction.BlockAction;
import evilcraft.api.config.elementtypeaction.DegradationEffectAction;
import evilcraft.api.config.elementtypeaction.DummyAction;
import evilcraft.api.config.elementtypeaction.EnchantmentAction;
import evilcraft.api.config.elementtypeaction.EntityAction;
import evilcraft.api.config.elementtypeaction.FluidAction;
import evilcraft.api.config.elementtypeaction.IElementTypeAction;
import evilcraft.api.config.elementtypeaction.ItemAction;
import evilcraft.api.config.elementtypeaction.MobAction;
import evilcraft.api.config.elementtypeaction.VillagerAction;

/**
 * The different types of {@link Configurable}.
 * @author rubensworks
 *
 */
public enum ElementType {
    
    /**
     * Item type.
     */
    ITEM(true, ItemConfig.class, new ItemAction(), ElementTypeCategory.ITEM),
    /**
     * Block type.
     */
    BLOCK(true, BlockConfig.class, new BlockAction(), ElementTypeCategory.BLOCK),
    /**
     * Block type with containers.
     */
    BLOCKCONTAINER(true, BlockConfig.class, new BlockAction(), ElementTypeCategory.BLOCK),
    /**
     * Mob type.
     */
    MOB(false, MobConfig.class, new MobAction(), ElementTypeCategory.MOB),
    /**
     * Regular entity type.
     */
    ENTITY(false, EntityConfig.class, new EntityAction(), ElementTypeCategory.ENTITY),
    /**
     * Fluid type.
     */
    FLUID(true, FluidConfig.class, new FluidAction(), ElementTypeCategory.FLUID),
    /**
     * Enchantment type.
     */
    ENCHANTMENT(true, EnchantmentConfig.class, new EnchantmentAction(), ElementTypeCategory.ENCHANTMENT),
    /**
     * Villager type.
     */
    VILLAGER(true, VillagerConfig.class, new VillagerAction(), ElementTypeCategory.MOB),
    /**
     * Biome type.
     */
    BIOME(true, BiomeConfig.class, new BiomeAction(), ElementTypeCategory.BIOME),
    /**
     * Degradation effect type.
     */
    DEGRADATIONEFFECT(true, DegradationEffectConfig.class, new DegradationEffectAction(), ElementTypeCategory.DEGRADATIONEFFECT),

    /**
     * Dummy type, only used for configs that refer to nothing.
     */
    DUMMY(false, DummyConfig.class, new DummyAction(), ElementTypeCategory.GENERAL);
    
    private boolean uniqueInstance = false;
    @SuppressWarnings("rawtypes")
    private Class<? extends ExtendedConfig> configClass;
    @SuppressWarnings("rawtypes")
    private IElementTypeAction action;
    private ElementTypeCategory category;
    
    @SuppressWarnings("rawtypes")
    private ElementType(boolean uniqueInstance, Class<? extends ExtendedConfig> configClass,
            IElementTypeAction action, ElementTypeCategory category) {
        this.uniqueInstance = uniqueInstance;
        this.configClass = configClass;
        this.action = action;
        this.category = category;
    }
    
    /**
     * If this type should refer to a {@link Configurable} with a unique instance.
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
     * The action for this type after the the {@link Configurable} has configured so it can be registered.
     * @return The action for this type.
     */
    @SuppressWarnings("rawtypes")
    public IElementTypeAction getElementTypeAction() {
        return action;
    }
    
    /**
     * The category of this type.
     * @return The category.
     */
    public String getCategory() {
        return category.toString();
    }
}
