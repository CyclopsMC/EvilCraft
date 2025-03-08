package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockLightningBomb}.
 * @author rubensworks
 *
 */
public class BlockLightningBombConfig extends BlockConfigCommon<IModBase> {

    public BlockLightningBombConfig() {
        super(
                EvilCraft._instance,
            "lightning_bomb",
                (eConfig, properties) -> new BlockLightningBomb(properties
                        .strength(0.0F)
                        .sound(SoundType.GRAVEL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
