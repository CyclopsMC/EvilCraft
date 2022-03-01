package org.cyclops.evilcraft.block;


import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
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
                    ObfuscationReflectionHelper.setPrivateValue(BlockBehaviour.class, block,
                            (Supplier<ResourceLocation>) () -> RegistryEntries.BLOCK_GEM_STONE_TORCH.getLootTable(), "lootTableSupplier");
                    return block;
                },
                null
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(getInstance(), RenderType.cutout());
    }

}
