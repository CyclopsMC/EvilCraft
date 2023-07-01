package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.Set;

/**
 * Config for the {@link BlockSpiritReanimator}.
 * @author rubensworks
 *
 */
public class BlockSpiritReanimatorConfig extends UpgradableBlockContainerConfig {

    @ConfigurableProperty(category = "machine", comment = "How much mB per tick this machine should consume.")
    public static int mBPerTick = 5;

    @ConfigurableProperty(category = "machine", comment = "The required amount of ticks for each reanimation.")
    public static int requiredTicks = 500;

    @ConfigurableProperty(category = "machine", comment = "If the Box of Eternal Closure should be cleared after a revival.")
    public static boolean clearBoxContents = true;

    public BlockSpiritReanimatorConfig() {
        super(
                EvilCraft._instance,
            "spirit_reanimator",
                eConfig -> new BlockSpiritReanimator(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        )
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
