package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityBloodStain}.
 * @author rubensworks
 *
 */
public class BlockEntityBloodStainConfig extends BlockEntityConfigCommon<BlockEntityBloodStain, IModBase> {

    public BlockEntityBloodStainConfig() {
        super(
                EvilCraft._instance,
                "blood_stain",
                (eConfig) -> new BlockEntityType<>(BlockEntityBloodStain::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_STAIN.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityBloodStain.CapabilityRegistrar(this::getInstance)::register);
    }

}
