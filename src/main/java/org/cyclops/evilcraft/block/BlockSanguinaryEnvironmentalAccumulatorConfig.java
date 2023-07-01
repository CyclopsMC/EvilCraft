package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;

import java.util.Set;

/**
 * Config for the {@link BlockSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class BlockSanguinaryEnvironmentalAccumulatorConfig extends UpgradableBlockContainerConfig {

    @ConfigurableProperty(category = "machine", isCommandable = true, comment = "The base blood usage in mB for recipes, this is multiplied with the cooldown time per recipe.")
    public static int baseUsage = 50;

    public BlockSanguinaryEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
            "sanguinary_environmental_accumulator",
                eConfig -> new BlockSanguinaryEnvironmentalAccumulator(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
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

    @Override
    protected String getConfigPropertyPrefix(ConfigurableProperty annotation) {
        return "sang_envir_acc";
    }
}
