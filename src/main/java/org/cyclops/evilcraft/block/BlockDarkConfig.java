package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the dark block.
 * @author rubensworks
 *
 */
public class BlockDarkConfig extends BlockConfigCommon<IModBase> {

    public BlockDarkConfig() {
        super(
                EvilCraft._instance,
            "dark_block",
                (eConfig, properties) -> new Block(properties
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.METAL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
