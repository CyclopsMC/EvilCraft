package org.cyclops.evilcraft.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

import java.util.Optional;

/**
 * Config for the Undead Sapling.
 * @author rubensworks
 *
 */
public class BlockUndeadSaplingConfig extends BlockConfig {

    public static final ResourceKey<ConfiguredFeature<?, ?>> UNDEAD_TREE = ResourceKey
            .create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "tree_undead"));
    public static final TreeGrower UNDEAD_TREE_GROWER = new TreeGrower(
            Reference.MOD_ID + ":menril_sapling",
            Optional.empty(), // Mega tree
            Optional.of(UNDEAD_TREE),
            Optional.empty() // Flowers
    );

    public BlockUndeadSaplingConfig() {
        super(
                EvilCraft._instance,
            "undead_sapling",
                eConfig -> new SaplingBlock(UNDEAD_TREE_GROWER, Block.Properties.of()
                        .noCollission()
                        .randomTicks()
                        .strength(0)
                        .sound(SoundType.GRASS)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
}
