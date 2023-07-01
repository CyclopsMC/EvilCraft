package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
                eConfig -> new BlockObscuredGlass(Block.Properties.of()
                        .strength(0.5F)
                        .sound(SoundType.GLASS)
                        .noOcclusion()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
