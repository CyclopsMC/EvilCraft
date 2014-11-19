package evilcraft.block;

import com.google.common.collect.Sets;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileWorking;
import net.minecraft.item.ItemBlock;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.item.ItemBlockNBT;

import java.util.Set;

/**
 * Config for the {@link SpiritReanimator}.
 * @author rubensworks
 *
 */
public class SpiritReanimatorConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static SpiritReanimatorConfig _instance;
    
    /**
     * How much mB per tick this machine should consume.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "How much mB per tick this machine should consume.")
    public static int mBPerTick = 5;
    
    /**
     * The required amount of ticks for each reanimation.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The required amount of ticks for each reanimation.")
    public static int requiredTicks = 500;
    
    /**
     * If the Box of Eternal Closure should be cleared after a revival.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If the Box of Eternal Closure should be cleared after a revival.")
    public static boolean clearBoxContents = true;

    /**
     * Make a new instance.
     */
    public SpiritReanimatorConfig() {
        super(
        	true,
            "spiritReanimator",
            null,
            SpiritReanimator.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }

    @Override
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet(
                TileWorking.UPGRADE_EFFICIENCY,
                TileWorking.UPGRADE_SPEED);
    }
    
}
