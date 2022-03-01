package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
                eConfig -> new BlockInvisibleRedstone(Block.Properties.of(Material.AIR)
                        .strength(5.0F, 10.0F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new BlockItem(block, new Item.Properties())
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(getInstance(), RenderType.cutout());
    }
    
}
