package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderItemStackTileEntityBloodChest;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;

import java.util.Set;

/**
 * Config for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class BlockColossalBloodChestConfig extends UpgradableBlockContainerConfig {

    @ConfigurableProperty(category = "machine", comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int baseMBPerDamage = 10;

    @ConfigurableProperty(category = "machine", comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;

    @ConfigurableProperty(category = "machine", comment = "The base amount of concurrent items that need to be available before efficiency can rise.", isCommandable = true)
    public static int baseConcurrentItems = 4;

    public BlockColossalBloodChestConfig() {
        super(
                EvilCraft._instance,
            "colossal_blood_chest",
                eConfig -> new BlockColossalBloodChest(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(5.0F)
                        .sound(SoundType.WOOD)
                        .harvestTool(ToolType.AXE)
                        .harvestLevel(2)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .setISTER(() -> RenderItemStackTileEntityBloodChest::new))
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
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

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getCutout());
    }
    
}
