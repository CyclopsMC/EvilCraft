package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.Set;

/**
 * Config for the {@link BlockBloodInfuser}.
 * @author rubensworks
 *
 */
public class BlockBloodInfuserConfig extends UpgradableBlockContainerConfig {

    public BlockBloodInfuserConfig() {
        super(
            EvilCraft._instance,
            "blood_infuser",
                eConfig -> new BlockBloodInfuser(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
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
