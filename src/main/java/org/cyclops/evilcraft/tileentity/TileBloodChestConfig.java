package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityBloodChest;

/**
 * Config for the {@link TileBloodChest}.
 * @author rubensworks
 *
 */
public class TileBloodChestConfig extends TileEntityConfig<TileBloodChest> {

    public TileBloodChestConfig() {
        super(
                EvilCraft._instance,
                "blood_chest",
                (eConfig) -> new TileEntityType<>(TileBloodChest::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_CHEST), null)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderTileEntityBloodChest::new);
    }

}
