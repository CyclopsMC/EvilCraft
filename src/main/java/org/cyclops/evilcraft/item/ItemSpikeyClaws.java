package org.cyclops.evilcraft.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A tool that can claw on blocks and entities.
 * @author rubensworks
 *
 */
public class ItemSpikeyClaws extends Item {

    public ItemSpikeyClaws(Properties properties) {
        super(properties.sword(ToolMaterial.IRON, 3, -2.4F));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return isCorrectToolForDrops(stack, state) ? super.getDestroySpeed(stack, state) * 2.0F : 0.1F;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_SHOVEL) || super.isCorrectToolForDrops(stack, state);
    }
}
