package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityDisplayStand}.
 * @author rubensworks
 *
 */
public class BlockEntityDisplayStandConfig extends BlockEntityConfigCommon<BlockEntityDisplayStand, IModBase> {

    public BlockEntityDisplayStandConfig() {
        super(
                EvilCraft._instance,
                "display_stand",
                (eConfig) -> new BlockEntityType<>(BlockEntityDisplayStand::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_DISPLAY_STAND.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityDisplayStand.CapabilityRegistrar(this::getInstance)::register);
    }

}
