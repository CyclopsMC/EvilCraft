package org.cyclops.evilcraft.core.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.configurable.IConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.world.gen.WorldGeneratorUndeadTree;

import java.util.List;
import java.util.Random;

/**
 * Block extending from a sapling that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockSapling extends BlockSapling implements IConfigurableBlock {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockState createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty(ignore = false)
    public static final IProperty[] _COMPAT = {STAGE};
    @BlockProperty(ignore = true)
    public static final IProperty[] _COMPAT_IGNORED = {TYPE};

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    protected boolean hasGui = false;

    private WorldGeneratorUndeadTree treeGenerator;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockSapling(ExtendedConfig eConfig, Material material) {
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        treeGenerator = new WorldGeneratorUndeadTree(true, this);
        setStepSound(soundTypeGrass);
    }

    @Override
    public boolean hasGui() {
        return hasGui;
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public void grow(World world, BlockPos blockPos, IBlockState blockState, Random random) {
        if (world.isRemote) {
            return;
        }

        world.setBlockToAir(blockPos);

        if(!treeGenerator.growTree(world, random, blockPos)) {
            world.setBlockState(blockPos, blockState, 4);
        }
    }

}
