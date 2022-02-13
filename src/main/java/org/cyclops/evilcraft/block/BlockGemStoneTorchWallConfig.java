package org.cyclops.evilcraft.block;


import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.function.Supplier;

/**
 * Config for the Burning Gemstone Torch Wall.
 * @author rubensworks
 *
 */
public class BlockGemStoneTorchWallConfig extends BlockConfig {

    public BlockGemStoneTorchWallConfig() {
        super(
                EvilCraft._instance,
            "gem_stone_torch_wall",
                eConfig -> {
                    WallTorchBlock block = new WallTorchBlock(Block.Properties.of(Material.DECORATION)
                            .noCollission()
                            .strength(0)
                            .lightLevel((state) -> 14)
                            .sound(SoundType.WOOD), ParticleTypes.FLAME);
                    ObfuscationReflectionHelper.setPrivateValue(AbstractBlock.class, block,
                            (Supplier<ResourceLocation>) () -> RegistryEntries.BLOCK_GEM_STONE_TORCH.getLootTable(), "lootTableSupplier");
                    return block;
                },
                null
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.cutout());
    }
    
}
