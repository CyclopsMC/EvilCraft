package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityInvisibleRedstone}.
 * @author rubensworks
 *
 */
public class BlockEntityInvisibleRedstoneConfig extends BlockEntityConfigCommon<BlockEntityInvisibleRedstone, IModBase> {

    public BlockEntityInvisibleRedstoneConfig() {
        super(
                EvilCraft._instance,
                "invisible_redstone",
                (eConfig) -> new BlockEntityType<>(BlockEntityInvisibleRedstone::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_INVISIBLE_REDSTONE.get()))
        );
    }

}
