package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Dark Brick Stairs.
 * @author rubensworks
 *
 */
public class BlockDarkBrickStairsConfig extends BlockConfigCommon<IModBase> {

    public BlockDarkBrickStairsConfig() {
        super(
                EvilCraft._instance,
                "dark_brick_stairs",
                (eConfig, properties) -> new StairBlock(RegistryEntries.BLOCK_DARK_BRICK.get().defaultBlockState(),
                        properties
                                .requiresCorrectToolForDrops()
                                .strength(5.0F)
                                .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
