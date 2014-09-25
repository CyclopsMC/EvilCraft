package evilcraft.entity.item;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.render.entity.RenderEntityItemDarkStick;
import evilcraft.core.config.extendedconfig.EntityConfig;

/**
 * Config for the {@link EntityItemDarkStick}.
 * @author rubensworks
 *
 */
public class EntityItemDarkStickConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityItemDarkStickConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemDarkStickConfig() {
        super(
        	true,
            "entityItemDarkStick",
            null,
            EntityItemDarkStick.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    protected Render getRender() {
        return new RenderEntityItemDarkStick();
    }
    
}
