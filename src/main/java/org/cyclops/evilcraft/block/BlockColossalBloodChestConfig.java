package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.Set;

/**
 * Config for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class BlockColossalBloodChestConfig extends UpgradableBlockContainerConfig {

    @ConfigurablePropertyCommon(category = "machine", comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int baseMBPerDamage = 5;

    @ConfigurablePropertyCommon(category = "machine", comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;

    @ConfigurablePropertyCommon(category = "machine", comment = "The base amount of concurrent items that need to be available before efficiency can rise.", isCommandable = true)
    public static int baseConcurrentItems = 1;

    public BlockColossalBloodChestConfig() {
        super(
                EvilCraft._instance,
            "colossal_blood_chest",
                (eConfig, properties) -> new BlockColossalBloodChest(properties
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .isValidSpawn((state, level, pos, type) -> false)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, eConfig.createDefaultItemProperties())
        );
    }

    @Override
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet(
                Upgrades.UPGRADE_EFFICIENCY,
                Upgrades.UPGRADE_SPEED,
                Upgrades.UPGRADE_TIER1,
                Upgrades.UPGRADE_TIER2,
                Upgrades.UPGRADE_TIER3);
    }

}
