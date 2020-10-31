package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityDisplayStand;

/**
 * Config for the {@link TileDisplayStand}.
 * @author rubensworks
 *
 */
public class TileDisplayStandConfig extends TileEntityConfig<TileDisplayStand> {

    public TileDisplayStandConfig() {
        super(
                EvilCraft._instance,
                "display_stand",
                (eConfig) -> new TileEntityType<>(TileDisplayStand::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_DISPLAY_STAND), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        EvilCraft._instance.getProxy().registerRenderer(getInstance(), RenderTileEntityDisplayStand::new);
    }

}
