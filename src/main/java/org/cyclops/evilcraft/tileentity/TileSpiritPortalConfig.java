package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntitySpiritPortal;

/**
 * Config for the {@link TileSpiritPortal}.
 * @author rubensworks
 *
 */
public class TileSpiritPortalConfig extends TileEntityConfig<TileSpiritPortal> {

    public TileSpiritPortalConfig() {
        super(
                EvilCraft._instance,
                "spirit_portal",
                (eConfig) -> new TileEntityType<>(TileSpiritPortal::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SPIRIT_PORTAL), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderTileEntitySpiritPortal::new);
    }

}
