package org.cyclops.evilcraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ItemBlockBoxOfEternalClosure;

import java.util.Collection;

/**
 * Config for the {@link BlockBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BlockBoxOfEternalClosureConfig extends BlockConfig {

    public BlockBoxOfEternalClosureConfig() {
        super(
            EvilCraft._instance,
            "box_of_eternal_closure",
                eConfig -> new BlockBoxOfEternalClosure(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new ItemBlockBoxOfEternalClosure(block, (new Item.Properties())
                        )
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        BlockBoxOfEternalClosure.boxOfEternalClosureFilled = new ItemStack(getItemInstance());
        BlockBoxOfEternalClosure.setVengeanceSwarmContent(BlockBoxOfEternalClosure.boxOfEternalClosureFilled);
    }

    @Override
    protected Collection<ItemStack> defaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((BlockBoxOfEternalClosure) getInstance()).fillItemCategory(list);
        return list;
    }

}
