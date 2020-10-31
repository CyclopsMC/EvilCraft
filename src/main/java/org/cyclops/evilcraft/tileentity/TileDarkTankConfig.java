package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDarkTankConfig;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityDarkTank;

/**
 * Config for the {@link TileDarkTank}.
 * @author rubensworks
 *
 */
public class TileDarkTankConfig extends TileEntityConfig<TileDarkTank> {

    public TileDarkTankConfig() {
        super(
                EvilCraft._instance,
                "dark_tank",
                (eConfig) -> new TileEntityType<>(TileDarkTank::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_DARK_TANK), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        if(!BlockDarkTankConfig.staticBlockRendering) {
            getMod().getProxy().registerRenderer(getInstance(), RenderTileEntityDarkTank::new);
        }
    }

}
