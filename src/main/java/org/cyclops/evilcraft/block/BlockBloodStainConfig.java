package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockBloodStain}.
 * @author rubensworks
 *
 */
public class BlockBloodStainConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "The amount of blood per HP (2HP = 1 heart) of the max mob health that will be added to this blockState when a mob dies from fall damage.", isCommandable = true)
    public static int bloodMBPerHP = 20;

    public BlockBloodStainConfig() {
        super(
                EvilCraft._instance,
                "blood_stain",
                eConfig -> new BlockBloodStain(Block.Properties.of(Material.CLAY)
                        .noCollission()
                        .strength(0.5F)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(getInstance(), RenderType.cutout());
    }
    
}
