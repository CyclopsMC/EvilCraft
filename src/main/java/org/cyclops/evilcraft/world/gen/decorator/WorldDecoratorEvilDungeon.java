package org.cyclops.evilcraft.world.gen.decorator;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author rubensworks
 */
public class WorldDecoratorEvilDungeon extends Placement<ChanceConfig> {

    public WorldDecoratorEvilDungeon(Function<Dynamic<?>, ? extends ChanceConfig> config) {
        super(config);
    }

    public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn, Random random, ChanceConfig configIn, BlockPos pos) {
        int i = configIn.chance;
        return IntStream.range(0, i).mapToObj((p_227448_3_) -> {
            int j = random.nextInt(16) + 8 + pos.getX();
            int k = random.nextInt(16) + 8 + pos.getZ();
            int l = random.nextInt(generatorIn.getMaxHeight());
            return new BlockPos(j, l, k);
        });
    }

}
