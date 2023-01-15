package org.cyclops.evilcraft.core.degradation.effect;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.core.degradation.StochasticDegradationEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Degradation effect that will terraform certain blocks into the area to
 * other blockState.
 * @author rubensworks
 *
 */
public class TerraformDegradation extends StochasticDegradationEffect {

    private static Map<Block, Map<BlockState, Integer>> TERRAFORMATIONS = Maps.newHashMap();
    private static final double CHANCE = 0.1D;

    private static Random random = new Random();

    private static void generateReplacements() {
        // Default replacement
        putReplacement(null, Blocks.COBBLESTONE.defaultBlockState(), 30);

        putReplacement(Blocks.STONE, Blocks.COBBLESTONE.defaultBlockState());

        putReplacement(Blocks.COBBLESTONE, Blocks.DIRT.defaultBlockState(), 10);
        putReplacement(Blocks.COBBLESTONE, Blocks.LAVA.defaultBlockState(), 30);

        putReplacement(Blocks.COAL_BLOCK, Blocks.DIAMOND_BLOCK.defaultBlockState(), 10000);

        putReplacement(Blocks.DIRT, Blocks.NETHERRACK.defaultBlockState(), 30);
        putReplacement(Blocks.GRASS, Blocks.NETHERRACK.defaultBlockState(), 20);
        putReplacement(Blocks.MYCELIUM, Blocks.NETHERRACK.defaultBlockState(), 5);
        putReplacement(Blocks.DIRT, Blocks.SAND.defaultBlockState());
        putReplacement(Blocks.GRASS, Blocks.SAND.defaultBlockState());
        putReplacement(Blocks.MYCELIUM, Blocks.SAND.defaultBlockState());
        putReplacement(Blocks.DIRT, Blocks.CLAY.defaultBlockState(), 20);
        putReplacement(Blocks.GRASS, Blocks.SAND.defaultBlockState(), 20);
        putReplacement(Blocks.MYCELIUM, Blocks.SAND.defaultBlockState(), 20);

        putReplacement(Blocks.NETHERRACK, RegistryEntries.BLOCK_INFESTED_NETHER_NETHERRACK.defaultBlockState(), 50);

        putReplacement(Blocks.SAND, null);

        putReplacement(Blocks.WATER, null);
    }

    private static final void putReplacement(Block key, BlockState value) {
        putReplacement(key, value, 0);
    }

    private static final void putReplacement(Block key, BlockState value, int chance) {
        Map<BlockState, Integer> mapValue = TERRAFORMATIONS.get(key);
        if(mapValue == null) {
            mapValue = new HashMap<BlockState, Integer>();
            TERRAFORMATIONS.put(key, mapValue);
        }
        mapValue.put(value, chance);
    }

    public TerraformDegradation(DegradationEffectConfig eConfig) {
        super(eConfig, CHANCE);
    }

    @Override
    public void runClientSide(IDegradable degradable) {

    }

    protected BlockState getReplacement(Block block) {
        // Delay map generation until runtime
        if (TERRAFORMATIONS.isEmpty()) {
            generateReplacements();
        }

        Map<BlockState, Integer> mapValue = TERRAFORMATIONS.get(block);
        if(mapValue == null) { // Fetch the default replacement.
            mapValue = TERRAFORMATIONS.get(null);
        }
        if(mapValue != null) {
            Object[] keys = mapValue.keySet().toArray();
            BlockState holder = (BlockState) keys[random.nextInt(keys.length)];
            Integer chance = mapValue.get(holder);
            if(chance == null || chance == 0 || random.nextInt(chance) == 0) {
                return holder;
            }
        }
        return null;
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        Level world = degradable.getDegradationWorld();

        BlockPos blockPos = LocationHelpers.getRandomPointInSphere(
                degradable.getLocation(), degradable.getRadius());

        Block block = world.getBlockState(blockPos).getBlock();
        BlockState replace = getReplacement(block);

        if(replace != null
                && !degradable.getLocation().equals(blockPos)
                && world.getBlockEntity(blockPos) == null) {
            if(replace.getBlock() == null) {
                world.removeBlock(blockPos, false);
            } else if(replace.getDestroySpeed(world, blockPos) > 0) {
                world.setBlock(blockPos, replace, MinecraftHelpers.BLOCK_NOTIFY_CLIENT | MinecraftHelpers.BLOCK_NOTIFY);
            }
        }
    }

}
