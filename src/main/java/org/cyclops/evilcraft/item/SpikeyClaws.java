package org.cyclops.evilcraft.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableItemSword;

import java.util.Set;

/**
 * A tool that can claw on blocks and entities.
 * @author rubensworks
 *
 */
public class SpikeyClaws extends ConfigurableItemSword {

    private static final Set<Material> APPLICABLE_MATERIALS = ImmutableSet.<Material>builder()
            .add(Material.grass)
            .add(Material.ground)
            .add(Material.leaves)
            .add(Material.plants)
            .add(Material.vine)
            .add(Material.sponge)
            .add(Material.cloth)
            .add(Material.sand)
            .add(Material.carpet)
            .add(Material.coral)
            .add(Material.snow)
            .add(Material.craftedSnow)
            .add(Material.cactus)
            .add(Material.clay)
            .add(Material.cake)
            .add(Material.web)
            .build();
    private static final Set<String> TOOL_CLASSES = ImmutableSet.of("shovel");

    private static SpikeyClaws _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpikeyClaws getInstance() {
        return _instance;
    }

    public SpikeyClaws(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, ToolMaterial.IRON);
        this.setMaxDamage(256);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        return canHarvestBlock(state, stack) ? super.getStrVsBlock(stack, state) * 2.0F : 0.1F;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return TOOL_CLASSES;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return APPLICABLE_MATERIALS.contains(state.getMaterial()) || super.canHarvestBlock(state, stack);
    }
}
