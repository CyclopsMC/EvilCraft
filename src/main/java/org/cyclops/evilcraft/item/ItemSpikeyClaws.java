package org.cyclops.evilcraft.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraftforge.common.ToolType;

import java.util.Set;

import net.minecraft.item.Item.Properties;

/**
 * A tool that can claw on blocks and entities.
 * @author rubensworks
 *
 */
public class ItemSpikeyClaws extends SwordItem {

    private static final Set<Material> APPLICABLE_MATERIALS = ImmutableSet.<Material>builder()
            .add(Material.GRASS)
            .add(Material.DIRT)
            .add(Material.LEAVES)
            .add(Material.PLANT)
            .add(Material.VEGETABLE)
            .add(Material.SPONGE)
            .add(Material.WOOL)
            .add(Material.SAND)
            .add(Material.CLOTH_DECORATION)
            .add(Material.CORAL)
            .add(Material.TOP_SNOW)
            .add(Material.SNOW)
            .add(Material.CACTUS)
            .add(Material.CLAY)
            .add(Material.CAKE)
            .add(Material.WEB)
            .build();

    public ItemSpikeyClaws(Properties properties) {
        super(ItemTier.IRON, 3, -2.4F, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return canHarvestBlock(stack, state) ? super.getDestroySpeed(stack, state) * 2.0F : 0.1F;
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        return Sets.newHashSet(ToolType.SHOVEL);
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        return APPLICABLE_MATERIALS.contains(state.getMaterial()) || super.canHarvestBlock(stack, state);
    }
}
