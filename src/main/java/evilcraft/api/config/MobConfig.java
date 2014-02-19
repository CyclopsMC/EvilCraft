package evilcraft.api.config;

import net.minecraft.client.renderer.entity.Render;

/**
 * Config for mobs.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class MobConfig extends ExtendedConfig<MobConfig>{

    /**
     * Make a new instance.
     * @param defaultId The default ID for the configurable.
     * @param name The name for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public MobConfig(int defaultId, String name, String namedId,
            String comment, Class<?> element) {
        super(defaultId, name, namedId, comment, element);
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
