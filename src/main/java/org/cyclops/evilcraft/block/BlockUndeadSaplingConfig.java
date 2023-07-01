package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.world.gen.TreeUndead;

/**
 * Config for the Undead Sapling.
 * @author rubensworks
 *
 */
public class BlockUndeadSaplingConfig extends BlockConfig {

    public BlockUndeadSaplingConfig() {
        super(
                EvilCraft._instance,
            "undead_sapling",
                eConfig -> new SaplingBlock(new TreeUndead(), Block.Properties.of()
                        .noCollission()
                        .randomTicks()
                        .strength(0)
                        .sound(SoundType.GRASS)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ComposterBlock.COMPOSTABLES.put(getItemInstance(), 0.3F);
    }

}
