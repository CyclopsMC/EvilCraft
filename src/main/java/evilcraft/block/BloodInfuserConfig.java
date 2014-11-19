package evilcraft.block;

import com.google.common.collect.Sets;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.item.ItemBlockNBT;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileWorking;
import net.minecraft.item.ItemBlock;

import java.util.Set;

/**
 * Config for the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class BloodInfuserConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BloodInfuserConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodInfuserConfig() {
        super(
        	true,
            "bloodInfuser",
            null,
            BloodInfuser.class
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
                TileWorking.UPGRADE_SPEED,
                TileWorking.UPGRADE_TIER1,
                TileWorking.UPGRADE_TIER2,
                TileWorking.UPGRADE_TIER3);
    }
    
}
