package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntitySpiritPortal;

/**
 * Config for the {@link BlockEntitySpiritPortal}.
 * @author rubensworks
 *
 */
public class BlockEntitySpiritPortalConfig extends BlockEntityConfig<BlockEntitySpiritPortal> {

    public BlockEntitySpiritPortalConfig() {
        super(
                EvilCraft._instance,
                "spirit_portal",
                (eConfig) -> new BlockEntityType<>(BlockEntitySpiritPortal::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SPIRIT_PORTAL), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderBlockEntitySpiritPortal::new);
    }

}
