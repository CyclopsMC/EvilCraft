package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockEntangledChaliceConfig;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityEntangledChalice;

/**
 * Config for the {@link TileEntangledChalice}.
 * @author rubensworks
 *
 */
public class TileEntangledChaliceConfig extends TileEntityConfig<TileEntangledChalice> {

    public TileEntangledChaliceConfig() {
        super(
                EvilCraft._instance,
                "entangled_chalice",
                (eConfig) -> new TileEntityType<>(TileEntangledChalice::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENTANGLED_CHALICE), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        if(!BlockEntangledChaliceConfig.staticBlockRendering) {
            EvilCraft._instance.getProxy().registerRenderer(getInstance(), RenderTileEntityEntangledChalice::new);
        }
    }

}
