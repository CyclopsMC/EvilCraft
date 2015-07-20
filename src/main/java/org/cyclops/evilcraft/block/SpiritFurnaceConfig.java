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

import java.util.Set;

/**
 * Config for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class SpiritFurnaceConfig extends UpgradableBlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static SpiritFurnaceConfig _instance;
    
    /**
     * How much mB per tick this furnace should consume.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "How much mB per tick this furnace should consume.")
    public static int mBPerTick = 25;
    
    /**
     * The required amount of ticks for each HP for cooking an entity.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The required amount of ticks for each HP for cooking an entity.")
    public static int requiredTicksPerHp = 10;

    /**
     * Make a new instance.
     */
    public SpiritFurnaceConfig() {
        super(
                EvilCraft._instance,
        	true,
            "spiritFurnace",
            null,
            SpiritFurnace.class
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
