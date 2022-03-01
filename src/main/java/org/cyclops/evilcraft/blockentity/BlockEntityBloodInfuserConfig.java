package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityBloodInfuser}.
 * @author rubensworks
 *
 */
public class BlockEntityBloodInfuserConfig extends BlockEntityConfig<BlockEntityBloodInfuser> {

    public BlockEntityBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser",
                (eConfig) -> new BlockEntityType<>(BlockEntityBloodInfuser::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_INFUSER), null)
        );
    }

}
