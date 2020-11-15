package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileSanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class TileSanguinaryPedestalConfig extends TileEntityConfig<TileSanguinaryPedestal> {

    public TileSanguinaryPedestalConfig() {
        super(
                EvilCraft._instance,
                "sanguinary_pedestal",
                (eConfig) -> new TileEntityType<>(TileSanguinaryPedestal::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SANGUINARY_PEDESTAL_0, RegistryEntries.BLOCK_SANGUINARY_PEDESTAL_1), null)
        );
    }

}
