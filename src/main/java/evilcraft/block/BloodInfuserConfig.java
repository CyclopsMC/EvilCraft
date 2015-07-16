package evilcraft.block;

import com.google.common.collect.Sets;
import evilcraft.EvilCraft;
import evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileWorking;
import evilcraft.tileentity.tickaction.bloodchest.BloodChestRepairActionRegistry;
import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.item.ItemBlockNBT;

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
