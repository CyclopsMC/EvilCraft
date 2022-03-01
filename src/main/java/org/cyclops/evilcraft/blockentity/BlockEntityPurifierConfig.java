package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntityPurifier;

/**
 * Config for the {@link BlockEntityPurifier}.
 * @author rubensworks
 *
 */
public class BlockEntityPurifierConfig extends BlockEntityConfig<BlockEntityPurifier> {

    public BlockEntityPurifierConfig() {
        super(
                EvilCraft._instance,
                "purifier",
                (eConfig) -> new BlockEntityType<>(BlockEntityPurifier::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_PURIFIER), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderBlockEntityPurifier::new);
    }

}
