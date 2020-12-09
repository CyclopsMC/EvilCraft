package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
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
                eConfig -> new BlockObscuredGlass(Block.Properties.create(Material.GLASS)
                        .hardnessAndResistance(0.5F)
                        .sound(SoundType.GLASS)
                        .notSolid()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getTranslucent());
    }
    
}
