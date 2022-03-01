package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityBloodStain}.
 * @author rubensworks
 *
 */
public class BlockEntityBloodStainConfig extends BlockEntityConfig<BlockEntityBloodStain> {

    public BlockEntityBloodStainConfig() {
        super(
                EvilCraft._instance,
                "blood_stain",
                (eConfig) -> new BlockEntityType<>(BlockEntityBloodStain::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_STAIN), null)
        );
    }

}
