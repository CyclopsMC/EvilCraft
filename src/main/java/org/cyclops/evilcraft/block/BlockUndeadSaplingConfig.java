package org.cyclops.evilcraft.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;


/**
 * Config for the Undead Sapling.
 * @author rubensworks
 *
 */
public class BlockUndeadSaplingConfig extends BlockConfigCommon<IModBase> {

    public static final ResourceKey<Feature> UNDEAD_TREE = ResourceKey
            .create(Registries.FEATURE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "tree_undead"));
    public static final TreeGrower UNDEAD_TREE_GROWER = new TreeGrower(
            Reference.MOD_ID + ":menril_sapling",
            WeightedList.of(UNDEAD_TREE),
            WeightedList.of(), // Mega trees
            WeightedList.of(), // Flower trees
            UNDEAD_TREE // Shortest tree, for the sapling's growth height check
    );

    public BlockUndeadSaplingConfig() {
        super(
                EvilCraft._instance,
            "undead_sapling",
                (eConfig, properties) -> new SaplingBlock(UNDEAD_TREE_GROWER, properties
                        .noCollision()
                        .randomTicks()
                        .strength(0)
                        .sound(SoundType.GRASS)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
}
