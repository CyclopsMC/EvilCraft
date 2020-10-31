package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityColossalBloodChest;

/**
 * Config for the {@link TileColossalBloodChest}.
 * @author rubensworks
 *
 */
public class TileColossalBloodChestConfig extends TileEntityConfig<TileColossalBloodChest> {

    public TileColossalBloodChestConfig() {
        super(
                EvilCraft._instance,
                "colossal_blood_chest",
                (eConfig) -> new TileEntityType<>(TileColossalBloodChest::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_COLOSSAL_BLOOD_CHEST), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        EvilCraft._instance.getProxy().registerRenderer(getInstance(), RenderTileEntityColossalBloodChest::new);
    }

}
