package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntityBloodChest;

/**
 * Config for the {@link BlockEntityBloodChest}.
 * @author rubensworks
 *
 */
public class BlockEntityBloodChestConfig extends BlockEntityConfig<BlockEntityBloodChest> {

    public BlockEntityBloodChestConfig() {
        super(
                EvilCraft._instance,
                "blood_chest",
                (eConfig) -> new BlockEntityType<>(BlockEntityBloodChest::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_CHEST), null)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderBlockEntityBloodChest::new);
    }

}
