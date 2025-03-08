package org.cyclops.evilcraft.block;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Burning Gemstone Torch.
 * @author rubensworks
 *
 */
public class BlockGemStoneTorchConfig extends BlockConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "block", comment = "The radius that will be kept spirit-proof.", isCommandable = true)
    public static int area = 15;

    public BlockGemStoneTorchConfig() {
        super(
                EvilCraft._instance,
            "gem_stone_torch",
                (eConfig, properties) -> new TorchBlock(ParticleTypes.FLAME, properties
                        .noCollission()
                        .strength(0)
                        .lightLevel((state) -> 14)
                        .sound(SoundType.WOOD)),
                (eConfig, block) -> new StandingAndWallBlockItem(block, RegistryEntries.BLOCK_GEM_STONE_TORCH_WALL.get(), Direction.DOWN, eConfig.createDefaultItemProperties())
        );
    }

}
