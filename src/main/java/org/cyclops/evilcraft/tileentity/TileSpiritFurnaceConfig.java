package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileSpiritFurnace}.
 * @author rubensworks
 *
 */
public class TileSpiritFurnaceConfig extends TileEntityConfig<TileSpiritFurnace> {

    public TileSpiritFurnaceConfig() {
        super(
                EvilCraft._instance,
                "spirit_furnace",
                (eConfig) -> new TileEntityType<>(TileSpiritFurnace::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SPIRIT_FURNACE), null)
        );
    }

}
