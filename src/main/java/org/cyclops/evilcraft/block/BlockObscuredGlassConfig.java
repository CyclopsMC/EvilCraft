package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link BlockObscuredGlass}.
 * @author rubensworks
 *
 */
public class BlockObscuredGlassConfig extends BlockConfigCommon<IModBase> {

    public BlockObscuredGlassConfig() {
        super(
                EvilCraft._instance,
            "obscured_glass",
                (eConfig, properties) -> new BlockObscuredGlass(properties
                        .strength(0.5F)
                        .sound(SoundType.GLASS)
                        .noOcclusion()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
