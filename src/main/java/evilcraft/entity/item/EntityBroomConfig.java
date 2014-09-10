package evilcraft.entity.item;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.render.entity.RenderBroom;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.EntityConfig;

/**
 * Config for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class EntityBroomConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityBroomConfig _instance;
    
    /**
     * Maximum number of blocks the position on the client can differ
     * from the position sent by the server (note: this value should
     * take into account the round trip delay between client and server
     * because the code does not handle that. So the position sent by the
     * server will always be an "old" position).
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ENTITY, isCommandable = true, comment = "Defines the maximum number of blocks the client's broom position can be out of sync with the position sent by the server.")
    public static double desyncThreshold = 1.0;
    
    /**
     * The maximum number of blocks the client can move the position of
     * the broom at a time once we notice there is a desync between the
     * position on the client and server.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ENTITY, isCommandable = true, comment = "Defines the maximum number of blocks the client's broom should move when we notice there is a desync between the client's and server's position.")
    public static double desyncCorrectionValue = 0.4;

    /**
     * Make a new instance.
     */
    public EntityBroomConfig() {
        super(
        	true,
            "broom",
            null,
            EntityBroom.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    protected Render getRender() {
        return new RenderBroom(this);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
    @Override
    public int getUpdateFrequency() {
        return 10;
    }
}
