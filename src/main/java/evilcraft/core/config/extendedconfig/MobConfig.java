package evilcraft.core.config.extendedconfig;

import evilcraft.core.config.ConfigurableType;
import net.minecraft.client.renderer.entity.Render;

/**
 * Config for mobs.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class MobConfig extends ExtendedConfig<MobConfig>{

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public MobConfig(boolean enabled, String namedId, String comment, Class<?> element) {
        super(enabled, namedId, comment, element);
    }
    
    @Override
	public String getUnlocalizedName() {
		return "entity.mob." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.MOB;
	}

    /**
     * @return If a spawn egg should be registered for this mob.
     */
    public boolean hasSpawnEgg() {
        return true;
    }
    
    /**
     * Get the background color of the spawn egg.
     * @return The spawn egg background color.
     */
    public abstract int getBackgroundEggColor();
    /**
     * Get the foreground color of the spawn egg.
     * @return The spawn egg foreground color.
     */
    public abstract int getForegroundEggColor();
    
    /**
     * Get the render for this configurable.
     * @return Get the render.
     */
    public abstract Render getRender();

}
