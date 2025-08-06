package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityPurifier}.
 * @author rubensworks
 *
 */
public class BlockEntityPurifierConfig extends BlockEntityConfigCommon<BlockEntityPurifier, IModBase> {

    public BlockEntityPurifierConfig() {
        super(
                EvilCraft._instance,
                "purifier",
                (eConfig) -> new BlockEntityType<>(BlockEntityPurifier::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_PURIFIER.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityPurifier.CapabilityRegistrar<>(this::getInstance)::register);
    }

}
