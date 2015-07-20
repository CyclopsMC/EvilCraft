package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileWorking;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.BloodChestRepairActionRegistry;

import java.util.Set;

/**
 * Config for the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class BloodInfuserConfig extends UpgradableBlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BloodInfuserConfig _instance;

    /**
     * The blacklisted items, by item name.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE,
            comment = "The blacklisted Blood Chest items, by item name.",
            changedCallback = BloodChestRepairActionRegistry.ItemBlacklistChanged.class)
    public static String[] itemBlacklist = new String[]{
            "minecraft:stick"
    };

    /**
     * Make a new instance.
     */
    public BloodInfuserConfig() {
        super(
            EvilCraft._instance,
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
