package evilcraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.client.render.entity.RenderControlledZombie;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.item.NecromancerStaffConfig;
import net.minecraft.client.renderer.entity.Render;

/**
 * Config for the {@link ControlledZombie}.
 * @author rubensworks
 *
 */
public class ControlledZombieConfig extends MobConfig {

    /**
     * The unique instance.
     */
    public static ControlledZombieConfig _instance;

    /**
     * Make a new instance.
     */
    public ControlledZombieConfig() {
        super(
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
    public Render getRender() {
        return new RenderControlledZombie(this);
    }
    
}
