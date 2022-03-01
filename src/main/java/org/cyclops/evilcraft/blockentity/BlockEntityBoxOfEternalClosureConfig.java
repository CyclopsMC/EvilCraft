package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntityBoxOfEternalClosure;

/**
 * Config for the {@link BlockEntityBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BlockEntityBoxOfEternalClosureConfig extends BlockEntityConfig<BlockEntityBoxOfEternalClosure> {

    public BlockEntityBoxOfEternalClosureConfig() {
        super(
                EvilCraft._instance,
                "box_of_eternal_closure",
                (eConfig) -> new BlockEntityType<>(BlockEntityBoxOfEternalClosure::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE), null)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRegistered() {
        EvilCraft._instance.getProxy().registerRenderer(getInstance(), RenderBlockEntityBoxOfEternalClosure::new);
    }

}
