package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityBoxOfEternalClosure;

/**
 * Config for the {@link TileBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class TileBoxOfEternalClosureConfig extends TileEntityConfig<TileBoxOfEternalClosure> {

    public TileBoxOfEternalClosureConfig() {
        super(
                EvilCraft._instance,
                "box_of_eternal_closure",
                (eConfig) -> new TileEntityType<>(TileBoxOfEternalClosure::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE), null)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRegistered() {
        EvilCraft._instance.getProxy().registerRenderer(getInstance(), RenderTileEntityBoxOfEternalClosure::new);
    }

}
