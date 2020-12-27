package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockUndeadLeaves}.
 * @author rubensworks
 *
 */
public class BlockUndeadLeavesConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "How much Blood (mB) can be produced at most as a Blood Stain on each random tick.")
    public static int maxBloodStainAmount = 25;

    public BlockUndeadLeavesConfig() {
        super(
                EvilCraft._instance,
            "undead_leaves",
                eConfig -> new BlockUndeadLeaves(Block.Properties.create(Material.LEAVES)
                        .hardnessAndResistance(0.5F)
                        .sound(SoundType.GROUND)
                        .notSolid()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BlockHelpers.setFireInfo(getInstance(), 30, 60);
    }

}
