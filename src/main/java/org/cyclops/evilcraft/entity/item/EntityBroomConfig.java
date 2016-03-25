package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderBroom;

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
                EvilCraft._instance,
        	true,
            "broomEntity",
            null,
            EntityBroom.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    protected Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderBroom(Minecraft.getMinecraft().getRenderManager(), this);
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
