package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Dark Blood Brick Stairs.
 * @author rubensworks
 *
 */
public class BlockDarkBloodBrickStairsConfig extends BlockConfig {

    public BlockDarkBloodBrickStairsConfig() {
        super(
                EvilCraft._instance,
                "dark_blood_brick_stairs",
                eConfig -> new StairBlock(() -> RegistryEntries.BLOCK_DARK_BLOOD_BRICK.defaultBlockState(),
                        Block.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(5.0F)
                                .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
