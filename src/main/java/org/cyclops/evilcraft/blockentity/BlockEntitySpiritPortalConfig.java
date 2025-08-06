package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntitySpiritPortal}.
 * @author rubensworks
 *
 */
public class BlockEntitySpiritPortalConfig extends BlockEntityConfigCommon<BlockEntitySpiritPortal, IModBase> {

    public BlockEntitySpiritPortalConfig() {
        super(
                EvilCraft._instance,
                "spirit_portal",
                (eConfig) -> new BlockEntityType<>(BlockEntitySpiritPortal::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SPIRIT_PORTAL.get()))
        );
    }

}
