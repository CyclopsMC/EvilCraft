package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntityColossalBloodChest;

/**
 * Config for the {@link BlockEntityColossalBloodChest}.
 * @author rubensworks
 *
 */
public class BlockEntityColossalBloodChestConfig extends BlockEntityConfig<BlockEntityColossalBloodChest> {

    public BlockEntityColossalBloodChestConfig() {
        super(
                EvilCraft._instance,
                "colossal_blood_chest",
                (eConfig) -> new BlockEntityType<>(BlockEntityColossalBloodChest::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_COLOSSAL_BLOOD_CHEST), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        EvilCraft._instance.getProxy().registerRenderer(getInstance(), RenderBlockEntityColossalBloodChest::new);
    }

}
