package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Bloody Cobblestone.
 * @author rubensworks
 *
 */
public class BlockBloodyCobblestoneConfig extends BlockConfigCommon<IModBase> {

    public BlockBloodyCobblestoneConfig() {
        super(
            EvilCraft._instance,
            "bloody_cobblestone",
                (eConfig, properties) -> new Block(properties
                        .requiresCorrectToolForDrops()
                        .strength(1.5F, 10.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
