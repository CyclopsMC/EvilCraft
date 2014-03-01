package evilcraft.entities.effect;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;
import evilcraft.api.render.RenderTexture;

/**
 * Config for the {@link EntityNeutronBeam}.
 * @author rubensworks
 *
 */
public class EntityNeutronBeamConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityNeutronBeamConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityNeutronBeamConfig() {
        super(
        	true,
            "entityNeutronBeam",
            null,
            EntityNeutronBeam.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderTexture(0.5F, new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + this.NAMEDID + ".png"));
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
