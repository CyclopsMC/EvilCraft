package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
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
                eConfig -> new BlockUndeadLeaves(Block.Properties.of()
                        .replaceable()
                        .strength(0.5F)
                        .sound(SoundType.GRAVEL)
                        .noOcclusion()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ComposterBlock.COMPOSTABLES.put(getItemInstance(), 0.3F);
    }

}
