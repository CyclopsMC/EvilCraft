package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link BlockObscuredGlass}.
 * @author rubensworks
 *
 */
public class BlockObscuredGlassConfig extends BlockConfig {

    public BlockObscuredGlassConfig() {
        super(
                EvilCraft._instance,
            "obscured_glass",
                eConfig -> new BlockObscuredGlass(Block.Properties.of(Material.GLASS)
                        .strength(0.5F)
                        .sound(SoundType.GLASS)
                        .noOcclusion()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(getInstance(), RenderType.translucent());
    }
    
}
