package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderControlledZombie;
import org.cyclops.evilcraft.item.NecromancerStaffConfig;

/**
 * Config for the {@link ControlledZombie}.
 * @author rubensworks
 *
 */
public class ControlledZombieConfig extends MobConfig<ControlledZombie> {

    /**
     * The unique instance.
     */
    public static ControlledZombieConfig _instance;

    /**
     * Make a new instance.
     */
    public ControlledZombieConfig() {
        super(
                EvilCraft._instance,
        	true,
            "controlledZombie",
            null,
            ControlledZombie.class
        );
    }
    
    @Override
    public boolean isEnabled() {
        return Configs.isEnabled(NecromancerStaffConfig.class);
    }

    @Override
    public boolean hasSpawnEgg() {
        return false;
    }

    @Override
    public int getBackgroundEggColor() {
        return 0;
    }

    @Override
    public int getForegroundEggColor() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render<ControlledZombie> getRender(RenderManager renderManager) {
        return new RenderControlledZombie(this, renderManager);
    }
    
}
