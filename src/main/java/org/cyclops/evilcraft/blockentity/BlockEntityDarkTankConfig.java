package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityDarkTank}.
 * @author rubensworks
 *
 */
public class BlockEntityDarkTankConfig extends BlockEntityConfigCommon<BlockEntityDarkTank, IModBase> {

    public BlockEntityDarkTankConfig() {
        super(
                EvilCraft._instance,
                "dark_tank",
                (eConfig) -> new BlockEntityType<>(BlockEntityDarkTank::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_DARK_TANK.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityDarkTank.CapabilityRegistrar<>(this::getInstance)::register);
    }

}
