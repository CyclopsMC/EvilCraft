package org.cyclops.evilcraft.block;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the Burning Gemstone Torch Wall.
 * @author rubensworks
 *
 */
public class BlockGemStoneTorchWallConfig extends BlockConfigCommon<IModBase> {

    public BlockGemStoneTorchWallConfig() {
        super(
                EvilCraft._instance,
            "gem_stone_torch_wall",
                (eConfig, properties) -> new WallTorchBlock(ParticleTypes.FLAME, properties
                        .noCollision()
                        .strength(0)
                        .lightLevel((state) -> 14)
                        .sound(SoundType.WOOD)),
                null
        );
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        return Collections.emptyList();
    }
}
