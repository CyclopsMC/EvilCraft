package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityEntangledChalice}.
 * @author rubensworks
 *
 */
public class BlockEntityEntangledChaliceConfig extends BlockEntityConfigCommon<BlockEntityEntangledChalice, IModBase> {

    public BlockEntityEntangledChaliceConfig() {
        super(
                EvilCraft._instance,
                "entangled_chalice",
                (eConfig) -> new BlockEntityType<>(BlockEntityEntangledChalice::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENTANGLED_CHALICE.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityEntangledChalice.CapabilityRegistrar(this::getInstance)::register);
    }

}
