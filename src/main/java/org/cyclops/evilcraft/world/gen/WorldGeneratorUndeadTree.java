package org.cyclops.evilcraft.world.gen;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import org.cyclops.cyclopscore.world.gen.WorldGeneratorTree;
import org.cyclops.evilcraft.block.UndeadLeaves;
import org.cyclops.evilcraft.block.UndeadLogConfig;
import org.cyclops.evilcraft.block.UndeadSapling;

/**
 * WorldGenerator for Undead Trees.
 * Inspired by MFR tree generator
 * @author rubensworks
 *
 */
public class WorldGeneratorUndeadTree extends WorldGeneratorTree {

    /**
     * Make a new instance.
     * @param doNotify If the generator should notify the world.
     */
    public WorldGeneratorUndeadTree(boolean doNotify) {
        super(doNotify);
    }

    @Override
    protected int baseHeight() {
        return 9;
    }

    @Override
    protected int baseHeightRandomRange() {
        return 4;
    }

    @Override
    public BlockLeaves getLeaves() {
        return UndeadLeaves.getInstance();
    }

    @Override
    public BlockLog getLogs() {
        return (BlockLog) UndeadLogConfig._instance.getBlockInstance();
    }

    @Override
    public BlockSapling getSapling() {
        return UndeadSapling.getInstance();
    }
}
