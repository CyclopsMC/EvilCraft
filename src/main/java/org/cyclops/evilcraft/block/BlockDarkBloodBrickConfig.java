package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockDarkBloodBrick}.
 * @author rubensworks
 *
 */
public class BlockDarkBloodBrickConfig extends BlockConfigCommon<IModBase> {

    public BlockDarkBloodBrickConfig() {
        super(
                EvilCraft._instance,
            "dark_blood_brick",
                (eConfig, properties) -> new BlockDarkBloodBrick(properties
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.STONE)
                        .isValidSpawn((_, _, _, _) -> false)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
