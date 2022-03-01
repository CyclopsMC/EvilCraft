package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntitySpiritReanimator}.
 * @author rubensworks
 *
 */
public class BlockEntitySpiritReanimatorConfig extends BlockEntityConfig<BlockEntitySpiritReanimator> {

    public BlockEntitySpiritReanimatorConfig() {
        super(
                EvilCraft._instance,
                "spirit_reanimator",
                (eConfig) -> new BlockEntityType<>(BlockEntitySpiritReanimator::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SPIRIT_REANIMATOR), null)
        );
    }

}
