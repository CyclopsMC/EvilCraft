package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.blockentity.RenderItemStackBlockEntityBloodChest;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Config for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class BlockColossalBloodChestConfig extends UpgradableBlockContainerConfig {

    @ConfigurableProperty(category = "machine", comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int baseMBPerDamage = 5;

    @ConfigurableProperty(category = "machine", comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;

    @ConfigurableProperty(category = "machine", comment = "The base amount of concurrent items that need to be available before efficiency can rise.", isCommandable = true)
    public static int baseConcurrentItems = 1;

    public BlockColossalBloodChestConfig() {
        super(
                EvilCraft._instance,
            "colossal_blood_chest",
                eConfig -> new BlockColossalBloodChest(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.WOOD)
                        .noOcclusion()),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        ) {
                    @Override
                    @OnlyIn(Dist.CLIENT)
                    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                        consumer.accept(new RenderItemStackBlockEntityBloodChest.ItemRenderProperties());
                    }
                }
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
