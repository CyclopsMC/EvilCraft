package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntityDisplayStand;

/**
 * Config for the {@link BlockEntityDisplayStand}.
 * @author rubensworks
 *
 */
public class BlockEntityDisplayStandConfig extends BlockEntityConfig<BlockEntityDisplayStand> {

    public BlockEntityDisplayStandConfig() {
        super(
                EvilCraft._instance,
                "display_stand",
                (eConfig) -> new BlockEntityType<>(BlockEntityDisplayStand::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_DISPLAY_STAND), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        EvilCraft._instance.getProxy().registerRenderer(getInstance(), RenderBlockEntityDisplayStand::new);
    }

}
