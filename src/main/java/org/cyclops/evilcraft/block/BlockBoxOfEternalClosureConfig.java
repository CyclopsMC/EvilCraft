package org.cyclops.evilcraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ItemBlockBoxOfEternalClosure;

import java.util.Collection;

/**
 * Config for the {@link BlockBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BlockBoxOfEternalClosureConfig extends BlockConfigCommon<ModBaseNeoForge<?>> {

    public BlockBoxOfEternalClosureConfig() {
        super(
            EvilCraft._instance,
            "box_of_eternal_closure",
                (eConfig, properties) -> new BlockBoxOfEternalClosure(properties
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new ItemBlockBoxOfEternalClosure(block, eConfig.createDefaultItemProperties()
                        )
        );
    }

    @Override
    public void onRegistryRegistered() {
        super.onRegistryRegistered();

        BlockBoxOfEternalClosure.boxOfEternalClosureFilled = new ItemStack(getItemInstance());
        BlockBoxOfEternalClosure.setVengeanceSwarmContent(BlockBoxOfEternalClosure.boxOfEternalClosureFilled);
    }

    @Override
    public BlockClientConfig<ModBaseNeoForge<?>> constructBlockClientConfig() {
        return new BlockBoxOfEternalClosureConfigClient(this);
    }

    @Override
    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((BlockBoxOfEternalClosure) getInstance()).fillItemCategory(list);
        return list;
    }

}
