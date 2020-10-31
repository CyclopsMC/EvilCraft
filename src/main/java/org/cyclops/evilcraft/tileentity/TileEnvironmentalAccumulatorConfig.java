package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityEnvironmentalAccumulator;

/**
 * Config for the {@link TileEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class TileEnvironmentalAccumulatorConfig extends TileEntityConfig<TileEnvironmentalAccumulator> {

    public TileEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                (eConfig) -> new TileEntityType<>(TileEnvironmentalAccumulator::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR), null)
        );
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderTileEntityEnvironmentalAccumulator::new);
    }

}
