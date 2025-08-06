package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BlockEntityBoxOfEternalClosureConfig extends BlockEntityConfigCommon<BlockEntityBoxOfEternalClosure, IModBase> {

    public BlockEntityBoxOfEternalClosureConfig() {
        super(
                EvilCraft._instance,
                "box_of_eternal_closure",
                (eConfig) -> new BlockEntityType<>(BlockEntityBoxOfEternalClosure::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.get()))
        );
    }

}
