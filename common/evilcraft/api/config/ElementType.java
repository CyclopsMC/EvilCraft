package evilcraft.api.config;

import evilcraft.api.config.elementtypeaction.BlockAction;
import evilcraft.api.config.elementtypeaction.DummyAction;
import evilcraft.api.config.elementtypeaction.EnchantmentAction;
import evilcraft.api.config.elementtypeaction.EntityAction;
import evilcraft.api.config.elementtypeaction.FluidAction;
import evilcraft.api.config.elementtypeaction.IElementTypeAction;
import evilcraft.api.config.elementtypeaction.ItemAction;
import evilcraft.api.config.elementtypeaction.MobAction;
import evilcraft.api.config.elementtypeaction.VillagerAction;

/**
 * Types of elements
 * @author Ruben Taelman
 *
 */
public enum ElementType {
    
    ITEM(true, ItemConfig.class, new ItemAction(), ElementTypeCategory.ITEM),
    BLOCK(true, BlockConfig.class, new BlockAction(), ElementTypeCategory.BLOCK),
    BLOCKCONTAINER(true, BlockConfig.class, new BlockAction(), ElementTypeCategory.BLOCK),
    MOB(false, MobConfig.class, new MobAction(), ElementTypeCategory.MOB),
    ENTITY(false, EntityConfig.class, new EntityAction(), ElementTypeCategory.ENTITY),
    FLUID(true, FluidConfig.class, new FluidAction(), ElementTypeCategory.LIQUID),
    ENCHANTMENT(true, EnchantmentConfig.class, new EnchantmentAction(), ElementTypeCategory.ENCHANTMENT),
    VILLAGER(false, VillagerConfig.class, new VillagerAction(), ElementTypeCategory.MOB),
    
    DUMMY(false, DummyConfig.class, new DummyAction(), ElementTypeCategory.GENERAL);
    
    private boolean uniqueInstance = false;
    private Class<? extends ExtendedConfig> configClass;
    private IElementTypeAction action;
    private ElementTypeCategory category;
    
    private ElementType(boolean uniqueInstance, Class<? extends ExtendedConfig> configClass,
            IElementTypeAction action, ElementTypeCategory category) {
        this.uniqueInstance = uniqueInstance;
        this.configClass = configClass;
        this.action = action;
        this.category = category;
    }
    
    public boolean hasUniqueInstance() {
        return uniqueInstance;
    }
    
    public Class<? extends ExtendedConfig> getConfigClass() {
        return configClass;
    }
    
    public IElementTypeAction getElementTypeAction() {
        return action;
    }
    
    public String getCategory() {
        return category.toString();
    }
}
