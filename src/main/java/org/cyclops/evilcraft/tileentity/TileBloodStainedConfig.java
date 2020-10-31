package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileBloodStained}.
 * @author rubensworks
 *
 */
public class TileBloodStainedConfig extends TileEntityConfig<TileBloodStained> {

    public TileBloodStainedConfig() {
        super(
                EvilCraft._instance,
                "blood_stained",
                (eConfig) -> new TileEntityType<>(TileBloodStained::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_STAINED), null)
        );
    }

}
