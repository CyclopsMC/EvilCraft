package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntitySpiritFurnace}.
 * @author rubensworks
 *
 */
public class BlockEntitySpiritFurnaceConfig extends BlockEntityConfigCommon<BlockEntitySpiritFurnace, IModBase> {

    public BlockEntitySpiritFurnaceConfig() {
        super(
                EvilCraft._instance,
                "spirit_furnace",
                (eConfig) -> new BlockEntityType<>(BlockEntitySpiritFurnace::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SPIRIT_FURNACE.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntitySpiritFurnace.CapabilityRegistrar(this::getInstance)::register);
    }

}
