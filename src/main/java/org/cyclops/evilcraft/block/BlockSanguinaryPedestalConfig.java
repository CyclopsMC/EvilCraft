package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

/**
 * Config for the {@link BlockSanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class BlockSanguinaryPedestalConfig extends BlockConfig {

    @ConfigurableProperty(category = "item", comment = "Blood multiplier when Efficiency is active.", isCommandable = true)
    public static double efficiencyBoost = 1.5D;

    public BlockSanguinaryPedestalConfig(int tier) {
        super(
                EvilCraft._instance,
            "sanguinary_pedestal_" + tier,
                eConfig -> new BlockSanguinaryPedestal(Block.Properties.create(Material.IRON)
                        .hardnessAndResistance(2.5F)
                        .sound(SoundType.STONE)
                        .notSolid(), tier),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getCutout());
    }
    
}
