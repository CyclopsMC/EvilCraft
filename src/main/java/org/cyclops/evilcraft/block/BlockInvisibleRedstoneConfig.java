package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * A config for {@link BlockInvisibleRedstone}.
 * @author rubensworks
 *
 */
public class BlockInvisibleRedstoneConfig extends BlockConfig {

    public BlockInvisibleRedstoneConfig() {
        super(
                EvilCraft._instance,
        		"invisible_redstone",
                eConfig -> new BlockInvisibleRedstone(Block.Properties.create(Material.AIR)
                        .hardnessAndResistance(5.0F, 10.0F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new BlockItem(block, new Item.Properties())
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getCutout());
    }
    
}
