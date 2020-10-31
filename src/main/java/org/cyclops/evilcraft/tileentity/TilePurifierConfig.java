package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityPurifier;

/**
 * Config for the {@link TilePurifier}.
 * @author rubensworks
 *
 */
public class TilePurifierConfig extends TileEntityConfig<TilePurifier> {

    public TilePurifierConfig() {
        super(
                EvilCraft._instance,
                "purifier",
                (eConfig) -> new TileEntityType<>(TilePurifier::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_PURIFIER), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderTileEntityPurifier::new);
    }

}
