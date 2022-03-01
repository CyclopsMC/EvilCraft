package org.cyclops.evilcraft.block;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Burning Gemstone Torch.
 * @author rubensworks
 *
 */
public class BlockGemStoneTorchConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "The radius that will be kept spirit-proof.", isCommandable = true)
    public static int area = 15;

    public BlockGemStoneTorchConfig() {
        super(
                EvilCraft._instance,
            "gem_stone_torch",
                eConfig -> new TorchBlock(Block.Properties.of(Material.DECORATION)
                        .noCollission()
                        .strength(0)
                        .lightLevel((state) -> 14)
                        .sound(SoundType.WOOD), ParticleTypes.FLAME),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(getInstance(), RenderType.cutout());
    }

}
