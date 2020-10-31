package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileInvisibleRedstone}.
 * @author rubensworks
 *
 */
public class TileInvisibleRedstoneConfig extends TileEntityConfig<TileInvisibleRedstone> {

    public TileInvisibleRedstoneConfig() {
        super(
                EvilCraft._instance,
                "invisible_redstone_block",
                (eConfig) -> new TileEntityType<>(TileInvisibleRedstone::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_INVISIBLE_REDSTONE), null)
        );
    }

}
