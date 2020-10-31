package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileSpiritReanimator}.
 * @author rubensworks
 *
 */
public class TileSpiritReanimatorConfig extends TileEntityConfig<TileSpiritReanimator> {

    public TileSpiritReanimatorConfig() {
        super(
                EvilCraft._instance,
                "spirit_reanimator",
                (eConfig) -> new TileEntityType<>(TileSpiritReanimator::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SPIRIT_REANIMATOR), null)
        );
    }

}
