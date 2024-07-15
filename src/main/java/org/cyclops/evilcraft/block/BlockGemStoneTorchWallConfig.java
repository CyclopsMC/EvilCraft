package org.cyclops.evilcraft.block;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Collection;
import java.util.Collections;

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
                eConfig -> new WallTorchBlock(ParticleTypes.FLAME, Block.Properties.of()
                        .noCollission()
                        .strength(0)
                        .lightLevel((state) -> 14)
                        .sound(SoundType.WOOD)
                        .lootFrom(RegistryEntries.BLOCK_GEM_STONE_TORCH::get)),
                null
        );
    }

    @Override
    protected Collection<ItemStack> defaultCreativeTabEntries() {
        return Collections.emptyList();
    }
}
